#########################################blocking IO
# import socket
#
# sk=socket.socket()
#
# sk.connect(("127.0.0.1",8080))
#
# while 1:
#     data=sk.recv(1024)
#     print(data.decode("utf8"))
#     sk.send(b"hello server")


########################################################nonblocking IO
# import time
# import socket
# sk = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
#
# while True:
#     sk.connect(('127.0.0.1',6667))
#     print("hello")
#     sk.sendall(bytes("hello","utf8"))
#     time.sleep(2)
#     break

########################################################io多路复用
# import socket
#
# sk=socket.socket()
#
# sk.connect(("127.0.0.1",9904))
#
# while 1:
#     inp=input(">>").strip()
#     sk.send(inp.encode("utf8"))
#     data=sk.recv(1024)
#     print(data.decode("utf8"))
###############################################client.py
import socket

sk = socket.socket()
sk.connect(('127.0.0.1', 8801))

while True:
    inp = input(">>>>")
    sk.sendall(bytes(inp, "utf8"))
    data = sk.recv(1024)
    print(str(data, 'utf8'))