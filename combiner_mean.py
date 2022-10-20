#!/usr/bin/env python3
import sys

chunk_id = None
current_sum = 0
current_size = 0

for line in sys.stdin:
    current_chunk_id, value = line.split("\t")
    value = int(value)
        
    if chunk_id:
        if current_chunk_id == chunk_id:
            current_size += 1
            current_sum += value
        else:
            print(current_size, current_sum / current_size, sep="\t")
            chunk_id = current_chunk_id
            current_size = 1
            current_sum = value
    else:
        chunk_id = current_chunk_id
        current_size = 1
        current_sum = value