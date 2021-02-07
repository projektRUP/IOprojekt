#!/usr/bin/env python
# coding: utf-8

import socket
import sys
from array import array
from os import stat

#Konwersja pliku audio na tablice binarna
file_name = "example.wav"
arr = array('B') # create binary array to hold the wave file
result = stat(file_name)  # sample file is in the same folder
f = open(file_name, 'rb')
arr.fromfile(f, result.st_size) # using file size as the array length
print("Length of data: " + str(len(arr)))
f.close()

#Ustawienia TCP
TCP_IP = '127.0.0.1'
TCP_PORT = 5000
BUFFER_SIZE = 1024
MESSAGE = "Sending audio file "+file_name

#Utworzenie gniazda
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect((TCP_IP, TCP_PORT))

#Wysylanie dlugosci pliku i oczekiwanie na odpowiedz
s.send(str(len(arr)))
data = s.recv(BUFFER_SIZE)

#Wysylanie pliku
print(MESSAGE)
s.send(arr)

#Odbieranie odpowiedzi
data = s.recv(BUFFER_SIZE)
s.close()

print "received data:", data
