import socket

def main():
    # HOST="192.168.21.14"
    HOST = "127.0.0.1"
    PORT = 2000

    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.connect((HOST, PORT))
    print("Conexión Exitosa")

    vidas = 4
    acertado = True

    while vidas > 0 and acertado:
        numero = input("Dame un número del 1 al 10: ")
        s.send(numero.encode())

        data = s.recv(1024)
        data = data.decode()

        if data == "ENHORABUENA HAS ACERTADO!!!":
            print(data)
            s.close()
            acertado = False
        else:
            print(data)
            
        vidas = vidas - 1
        
    if acertado:
        print("HAS PERDIDO, NO HAS ACERTADO EL NÚMERO")
    s.close()

if __name__ == "__main__":
    main()
