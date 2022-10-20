#!/usr/bin/env python3
import sys

sum_ = 0
size_ = 0
mean = 0

for line in sys.stdin:
    current_size, value = line.split("\t")
    current_size = int(current_size)
    value = float(value)
    
    mean = (current_size * value + size_ * mean) / (current_size + size_)
    size_ += current_size
    
print(mean)