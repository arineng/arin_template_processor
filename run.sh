#!/usr/bin/env bash

set -e

docker ps -a | awk '{ print $1,$2 }' | grep arin-tp | awk '{print $1 }' | xargs -I {} docker rm {} || true
docker rmi -f $(docker images -q --filter=reference='arin-tp*') || true
./gradlew clean build buildDockerImage -x test --info --rerun-tasks
docker run --name arin-tp ${@:1} -it arin-tp
