#!/usr/bin/env bash

REPOSITORY=/product
cd $REPOSITORY

JAR_NAME=$(ls $REPOSITORY/target/ | grep '.jar' | head -n 1)
JAR_PATH=$REPOSITORY/target/$JAR_NAME

sudo kill -15 `sudo netstat -tnlp|grep 8080 |gawk '{ print $7 }'|grep -o '[0-9]*'`

nohup java -jar $JAR_PATH > /dev/null 2> /dev/null < /dev/null &
