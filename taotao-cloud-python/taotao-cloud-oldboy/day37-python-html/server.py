import socket


def main():
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    sock.bind(('localhost', 8080))
    sock.listen(5)

    while True:
        connection, address = sock.accept()
        buf = connection.recv(1024)
        f = open("test.html", "rb")
        data = f.read()
        connection.sendall(bytes("HTTP/1.1 201 OK\r\n\r\n", "utf8"))

        connection.sendall(data)

        connection.close()


if __name__ == '__main__':
    main()
