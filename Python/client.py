#!/usr/bin/env python
# coding: utf-8

import socket
import sys
from array import array
from os import stat

# converting audio file to binary array
file_name = "harvard.wav"
arr = array('B')  # create binary array to hold the wave file
result = stat(file_name)  # sample file is in the same folder
f = open(file_name, 'rb')
arr.fromfile(f, result.st_size)  # using file size as the array length
file_size = str(len(arr))
print("Length of data: " + file_size)
f.close()

# TCP settings
TCP_IP = '10.71.8.73'  # TODO: set server's IP address
TCP_PORT = 5000
BUFFER_SIZE = 1024
MESSAGE = "Sending audio file " + file_name
print(MESSAGE)

# creating socket
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect((TCP_IP, TCP_PORT))

# sending file length and waiting for response
b_file_size = file_size.encode('utf-8')
s.send(b_file_size)
data = s.recv(BUFFER_SIZE)

# sending file
counter = 0
with open('harvard.wav', 'rb') as f:  
    for l in f:
        counter = counter + 1
        s.sendall(l)

print("All packets send")

# receiving answer
data = s.recv(BUFFER_SIZE)
an_output = data.decode('utf-8')
s.close()

print("Received data: ", an_output)
