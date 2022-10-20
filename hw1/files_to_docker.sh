#!/usr/bin/env bash
set -x

docker cp run.sh  namenode:/
docker cp mapper_mean.py  namenode:/
docker cp reducer_mean.py  namenode:/
docker cp combiner_mean.py  namenode:/
docker cp data_no_header.csv namenode:/

docker exec -it namenode bash -c "chmod +x run.sh"
docker exec -it namenode bash -c "hdfs dfs -put data_no_header.csv"