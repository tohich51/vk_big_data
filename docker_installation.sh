#!/usr/bin/env bash
set -x

docker exec -it datanode1 bash -c "apt update && apt install python3 -y"
docker exec -it datanode2 bash -c "apt update && apt install python3 -y"
docker exec -it datanode3 bash -c "apt update && apt install python3 -y"
docker exec -it namenode bash -c "apt update && apt install python3 -y"
docker exec -it resourcemanager bash -c "apt update && apt install python3 -y"
docker exec -it nodemanager bash -c "apt update && apt install python3 -y"

docker exec -it datanode1 bash -c "apt install python3-numpy -y"
docker exec -it datanode2 bash -c "apt install python3-numpy -y"
docker exec -it datanode3 bash -c "apt install python3-numpy -y"
docker exec -it namenode bash -c "apt install python3-numpy -y"
docker exec -it resourcemanager bash -c "apt install python3-numpy -y"
docker exec -it nodemanager bash -c "apt install python3-numpy -y"