#!/usr/bin/env python3
import sys

CURRENT_SIZE = 1

sum_ = 0
size_ = 0
mean = 0
var = 0

for line in sys.stdin:
    chunkid, value = line.split("\t")
    
    value = float(value)
    
    mean = (CURRENT_SIZE * value + size_ * mean) / (CURRENT_SIZE + size_)
    size_ += CURRENT_SIZE
    var = (size_ * var) / (CURRENT_SIZE + size_) + size_ * ((value - mean) / (CURRENT_SIZE + size_)) ** 2
    
print(mean)
print(var)