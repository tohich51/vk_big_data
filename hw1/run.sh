#!/usr/bin/env bash
set -x

HADOOP_STREAMING_JAR=/opt/hadoop-3.2.1/share/hadoop/tools/lib/hadoop-streaming-3.2.1.jar

hdfs dfs -rm -r -skipTrash ${2}_mean
hdfs dfs -rm -r -skipTrash ${2}_var

yarn jar $HADOOP_STREAMING_JAR \
        -files mapper_mean.py,reducer_mean.py \
        -D mapred.job.name=${3} \
        -mapper 'python3 mapper_mean.py' \
        -combiner 'python3 combiner_mean.py'\
        -reducer 'python3 reducer_mean.py' \
        -input ${1} \
        -output ${2}_mean
        
yarn jar $HADOOP_STREAMING_JAR \
        -files mapper_mean.py,reducer_mean.py \
        -D mapred.job.name=${3} \
        -mapper 'python3 mapper_std.py' \
        -reducer 'python3 reducer_std.py' \
        -input ${1} \
        -output ${2}_var

hdfs dfs -cat ${2}_mean/part-00000 | head -n 1
hdfs dfs -cat ${2}_var/part-00000 | head -n 2
