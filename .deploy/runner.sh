#!/bin/bash

JAR_NAME="snackgame-server.jar"
PROCESS_ID=$(pgrep -f "$JAR_NAME")
PATH="/home/ubuntu/snackgame-server"

if [ -n "$PROCESS_ID" ]; then
  sudo kill $PROCESS_ID
  echo "\n🐣 구동중인 애플리케이션을 종료했습니다. (pid : $PROCESS_ID)\n"
fi

echo "\n🐣 SpringBoot 애플리케이션을 실행합니다.\n"

nohup java -jar $PATH/$JAR_NAME -DSpring.profiles.active=production > $PATH/spring.log &

exit
