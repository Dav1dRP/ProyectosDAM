import socket
import random
#HOST="192.168.21.13"
HOST="127.0.0.1"
PORT = 2000

s=socket.socket(socket.AF_INET,socket.SOCK_STREAM)
s.bind((HOST,PORT))
s.listen()
while True:
    conn,addr=s.accept()#Espera al Cliente
    numero=random.randint(1, 10)
    numero=numero
    print(f"Conexion con el cliente IP ({addr[0]}) Puerto:({addr[1]})")
    print(f"{numero}")
    data=conn.recv(1024)
    while(int(data)!=numero):

        if int(data)>int(numero):
            conn.send("El número es Menor".encode())
        if int(data)<int(numero):
            conn.send("El número es Mayor".encode())
        data=conn.recv(1024)

    conn.send("ENHORABUENA HAS ACERTADO!!!".encode())
    conn.close()