import socket
import random

def main():
    # HOST="192.168.21.13"
    HOST = "127.0.0.1"
    PORT = 2000

    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.bind((HOST, PORT))
    s.listen()

    while True:
        conn, addr = s.accept()  # Espera al Cliente
        numero = random.randint(1, 10)
        vidas = 4

        print(f"Conexion con el cliente IP ({addr[0]}) Puerto:({addr[1]})")

        while vidas > 0:
            data = conn.recv(1024)

            if int(data) == numero:
                conn.send("ENHORABUENA HAS ACERTADO!!!".encode())
                vidas = 0
            else:

                if int(data) > int(numero):
                    conn.send("El número es Menor".encode())

                elif int(data) < int(numero):
                    conn.send("El número es Mayor".encode())

                vidas = vidas - 1

        conn.close()

if __name__ == "__main__":
    main()
