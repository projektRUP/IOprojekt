import socket
import sys
import random

#Banalna symulacja outputu z sieci neruronowej
def dumbNeuralNetwork():
    output_data = random.randrange(0,2)
    return output_data

#Ustawienie hosta i portu
HOST = ''    # Symbolic name meaning all available interfaces
PORT = 5000    # Arbitrary non-privileged port

#Tworzenie gniazda
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
print 'Socket created'

#Bindowanie gniazda
try:
    s.bind((HOST, PORT))
except socket.error , msg:
    print 'Bind failed. Error Code : ' + str(msg[0]) + ' Message ' + msg[1]
    sys.exit()
print 'Socket bind complete'

#Nasluchiwanie
s.listen(10)

#Szeregowa obsluga wielu polaczen
while True:
    print 'Socket now listening'

    #Akceptowanie polaczenia
    conn, addr = s.accept()
    print 'Connected with ' + addr[0] + ':' + str(addr[1])

    #Wczytanie rozmiaru pliku do pobrania i potwierdzenie do klienta jego odbioru
    file_size = conn.recv(32)
    reply = "Przeslano rozmiar pliku"
    conn.sendall(reply)
    print("Rozmiar pliku: " + file_size)

    #Wyczyszczenie pliku roboczego
    f = open("newfile.wav", 'w+')
    f.write('')
    f.close()

    #Otwarcie pliku roboczego
    outfile = open("newfile.wav", 'ab')
    licznik = 0
    #Wczytanie danych do pliku
    while(licznik < (int(file_size)/1024)):
        data = conn.recv(1024)
        if not data: break
        outfile.write(data)
        licznik = licznik+1

    #Odpowiedz w postaci outputu z sieci neuronowej
    reply = str(dumbNeuralNetwork())
    conn.sendall(reply)

    #Zamkniecie poloczenia i pliku roboczego
    conn.close()
    outfile.close()
#Zamkniecie gniazda
s.close()
