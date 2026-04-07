#!/bin/bash
set -euo pipefail


if [ $# -ne 2 ]; then
  echo "Usage: $0 <http-backend-name> <https-backend-name>"
  exit 1
fi

HTTP_BACKEND="$1"
HTTPS_BACKEND="$2"


export PATH="$HOME/bin:$PATH"

COMPOSE_DIR="$HOME/snackgame-server"
HEALTH_CHECK_URL="http://localhost:8080/rankings/1?by=BEST_SCORE"
HEALTH_CHECK_TIMEOUT=120
HEALTH_CHECK_INTERVAL=5


: "${NLB_ID:?NLB_ID 환경변수가 설정되지 않았습니다}"
: "${ACCESS_TOKEN_SECRET_KEY:?ACCESS_TOKEN_SECRET_KEY 환경변수가 설정되지 않았습니다}"
: "${REFRESH_TOKEN_SECRET_KEY:?REFRESH_TOKEN_SECRET_KEY 환경변수가 설정되지 않았습니다}"
: "${DB_URL:?DB_URL 환경변수가 설정되지 않았습니다}"
: "${DB_USERNAME:?DB_USERNAME 환경변수가 설정되지 않았습니다}"
: "${DB_PASSWORD:?DB_PASSWORD 환경변수가 설정되지 않았습니다}"


set_drain() {
  local is_drain="$1"
  echo "[NLB] 드레인 설정: http=$HTTP_BACKEND, https=$HTTPS_BACKEND -> is-drain=$is_drain"

  oci nlb backend update \
    --auth instance_principal \
    --network-load-balancer-id "$NLB_ID" \
    --backend-set-name snackgame-http \
    --backend-name "$HTTP_BACKEND" \
    --is-drain "$is_drain" \
    --wait-for-state SUCCEEDED \
    --max-wait-seconds 120

  oci nlb backend update \
    --auth instance_principal \
    --network-load-balancer-id "$NLB_ID" \
    --backend-set-name snackgame-https \
    --backend-name "$HTTPS_BACKEND" \
    --is-drain "$is_drain" \
    --wait-for-state SUCCEEDED \
    --max-wait-seconds 120
}


wait_healthy() {
  echo "[헬스체크] 시작..."
  local elapsed=0
  local http_code="000"

  while [ "$elapsed" -lt "$HEALTH_CHECK_TIMEOUT" ]; do

    if [ "$(docker inspect -f '{{.State.Running}}' snackgame-server 2>/dev/null)" != "true" ]; then
      echo "[헬스체크] 컨테이너가 종료됨, 즉시 롤백"
      return 1
    fi
    http_code=$(curl -s -o /dev/null -w "%{http_code}" "$HEALTH_CHECK_URL" 2>/dev/null || echo "000")
    if [ "$http_code" = "200" ]; then
      echo "[헬스체크] 통과 (${elapsed}초)"
      return 0
    fi
    echo "[헬스체크] 대기 중... (${elapsed}초, 응답: $http_code)"
    sleep "$HEALTH_CHECK_INTERVAL"
    elapsed=$((elapsed + HEALTH_CHECK_INTERVAL))
  done

  echo "[헬스체크] 실패 - ${HEALTH_CHECK_TIMEOUT}초 초과"
  return 1
}


rollback() {
  echo "[롤백] 이전 컨테이너로 복구 중..."
  cd "$COMPOSE_DIR"
  docker compose up -d snackgame-server
  set_drain false
  echo "[롤백] 완료"
  exit 1
}


echo "======================================"
echo "[배포 시작]"
echo "[대상] HTTP=$HTTP_BACKEND, HTTPS=$HTTPS_BACKEND"
echo "======================================"

cd "$COMPOSE_DIR"


set_drain true

echo "기존 연결 종료 대기..."
sleep 15


echo "[Pull] 새 이미지 받는 중..."
docker compose pull


echo "[배포] 컨테이너 교체 중..."
docker compose up -d snackgame-server || rollback


wait_healthy || rollback


set_drain false


docker image prune -f

echo "======================================"
echo "[배포 완료]"
echo "======================================"