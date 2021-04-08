
#########################################blocking IO
# import socket
#
# sk=socket.socket()
#
# sk.bind(("127.0.0.1",8080))
#
# sk.listen(5)
#
# while 1:
#     conn,addr=sk.accept()
#
#     while 1:
#         conn.send("hello client".encode("utf8"))
#         data=conn.recv(1024)
#         print(data.decode("utf8"))

########################################################nonblocking IO

# import time
# import socket
# sk = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
# sk.bind(('127.0.0.1',6667))
# sk.listen(5)
# sk.setblocking(False)
# print ('waiting client connection .......')
# while True:
#     try:
#
#         connection,address = sk.accept()   # 进程主动轮询
#         print("+++",address)
#         client_messge = connection.recv(1024)
#         print(str(client_messge,'utf8'))
#         connection.close()
#     except Exception as e:
#         print (e)
#         time.sleep(4)

########################################################io多路复用

# import socket
# import select
# sk=socket.socket()
# sk.bind(("127.0.0.1",9904))
# sk.listen(5)
# inp=[sk,]
# while True:
#
#     r,w,e=select.select(inp,[],[],5) #[sk,conn]
#
#     for i in r:#[sk,]
#         conn,add=i.accept()
#         print(conn)
#         print("hello")
#         inp.append(conn)
#     print('>>>>>>')


#***********************server.py###################
import socket
import select
sk=socket.socket()
sk.bind(("127.0.0.1",8801))
sk.listen(5)
inputs=[sk,]
while True:
    r,w,e=select.select(inputs,[],[],5)

    for obj in r:#[sk,]
        if obj==sk:
            conn,add=obj.accept()
            print(conn)
            inputs.append(conn)
        else:
            data_byte=obj.recv(1024)
            print(str(data_byte,'utf8'))
            inp=input('回答%s号客户>>>'%inputs.index(obj))
            obj.sendall(bytes(inp,'utf8'))

    print('>>',r)

