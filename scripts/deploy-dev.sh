#!/bin/bash
set -euo pipefail


COMPOSE_DIR="$HOME/snackgame-server/snackgame-dev"
HEALTH_CHECK_URL="http://localhost:8081/rankings/1?by=BEST_SCORE"
HEALTH_CHECK_TIMEOUT=120
HEALTH_CHECK_INTERVAL=5


: "${ACCESS_TOKEN_SECRET_KEY:?ACCESS_TOKEN_SECRET_KEY 환경변수가 설정되지 않았습니다}"
: "${REFRESH_TOKEN_SECRET_KEY:?REFRESH_TOKEN_SECRET_KEY 환경변수가 설정되지 않았습니다}"
: "${DB_URL:?DB_URL 환경변수가 설정되지 않았습니다}"
: "${DB_USERNAME:?DB_USERNAME 환경변수가 설정되지 않았습니다}"
: "${DB_PASSWORD:?DB_PASSWORD 환경변수가 설정되지 않았습니다}"


wait_healthy() {
  echo "[헬스체크] 시작..."
  local elapsed=0
  local http_code="000"

  while [ "$elapsed" -lt "$HEALTH_CHECK_TIMEOUT" ]; do
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


echo "======================================"
echo "[Dev 배포 시작]"
echo "======================================"

cd "$COMPOSE_DIR"


echo "[Pull] 새 이미지 받는 중..."
docker compose pull snackgame-dev-server


echo "[배포] 컨테이너 교체 중..."
docker compose up -d --no-deps snackgame-dev-server


wait_healthy


docker image prune -f

echo "======================================"
echo "[Dev 배포 완료]"
echo "======================================"