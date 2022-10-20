#!/usr/bin/env python3
import sys
import numpy as np
import csv
    
for line in csv.reader(sys.stdin):
    chunk_id = np.random.randint(1, 100)
    print(chunk_id, line[9], sep="\t")