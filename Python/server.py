from __future__ import unicode_literals

import socket
import sys

import speech_recognition as sr
from googletrans import Translator
from textblob import TextBlob


# simulating output from audio analysis
def audio_analysis():
    r = sr.Recognizer()

    with sr.AudioFile('new_file.wav') as source:
        audio = r.record(source)
        try:
            print("Entered 'try', proceeding to transcribe")
            transcript = r.recognize_google(audio)
            print("Transcription finished")
        except:
            print("Transcription went wrong!")

    translator = Translator()
    translation = translator.translate(transcript, src='en', dest='en')  # TODO: change language to Polish if connected
    translated_text = translation.text

    blob = TextBlob(translated_text)

    output_data = 0.5 * (blob.sentiment.polarity + 1)

    return output_data


# host and port settings
HOST = ''  # symbolic name meaning all available interfaces
PORT = 5000  # arbitrary non-privileged port

# creating socket
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
print("Socket created")

# binding socket
try:
    s.bind((HOST, PORT))
except socket.error as msg:
    print("Bind failed. Error Code : " + str(msg[0]) + " Message " + msg[1])
    sys.exit()
print("Socket bind complete")

# listening
s.listen(10)

# serial multiple connections service
while True:
    print("Socket now listening")

    # accepting connection
    conn, addr = s.accept()
    print("Connected with " + addr[0] + ":" + str(addr[1]))

    # reading file size to download and confirming that file was received
    data = conn.recv(32)
    file_size = data.decode('utf-8')
    reply = "File size send"
    b_reply = reply.encode('utf-8')
    conn.sendall(b_reply)
    print("File size: " + file_size)

    # clearing work file
    f = open("new_file.wav", 'w+')
    f.write('')
    f.close()

    # work file opening
    outfile = open("new_file.wav", 'ab')
    bytes_received = 0

    # loading data into file
    while bytes_received < int(file_size):
        data = conn.recv(1024)
        if not data:
            break
        outfile.write(data)
        bytes_received = bytes_received + len(data)

    outfile.close()  # closing work file
    print("All packets received")

    reply = str(audio_analysis())
    b_reply = reply.encode('utf-8')
    conn.sendall(b_reply)
    print("Response send " + reply)

    conn.close()  # closing connection

# closing socket
s.close()
