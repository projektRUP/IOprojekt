import socket
import sys
import random


# simulating output from audio analysis
def audio_analysis():
    output_data = random.randrange(0, 2)
    return output_data


# host and port settings
HOST = ''  # symbolic name meaning all available interfaces
PORT = 5000  # arbitrary non-privileged port

# creating socket
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
print('Socket created')

# binding socket
try:
    s.bind((HOST, PORT))
except (socket.error, msg):
    print('Bind failed. Error Code : ' + str(msg[0]) + ' Message ' + msg[1])
    sys.exit()
print('Socket bind complete')

# listening
s.listen(10)

# serial multiple connections service
while True:
    print('Socket now listening')

    # accepting connection
    conn, addr = s.accept()
    print('Connected with ' + addr[0] + ':' + str(addr[1]))

    # reading file size to download and confirming that file was received
    file_size = conn.recv(32)
    reply = "File size send"
    conn.sendall(reply)
    print("File size: " + file_size)

    # clearing work file
    f = open("new_file.wav", 'w+')
    f.write('')
    f.close()

    # work file opening
    outfile = open("new_file.wav", 'ab')
    counter = 0

    # loading data into file
    while counter < (int(file_size)/1024):
        data = conn.recv(1024)
        if not data:
            break
        outfile.write(data)
        counter = counter + 1

    # reply (audio analysis output)
    reply = str(audio_analysis())
    conn.sendall(reply)

    # closing connection and work file
    conn.close()
    outfile.close()
# closing socket
s.close()
