# import socket
from socket import *
ip_port=('127.0.0.1',8080)
back_log=5
buffer_size=1024

tcp_client=socket(AF_INET,SOCK_STREAM)
tcp_client.connect(ip_port)

while True:
    msg=input('>>: ').strip()
    if not msg:continue
    tcp_client.send(msg.encode('utf-8'))
    print('客户端已经发送消息')
    data=tcp_client.recv(buffer_size)
    print('收到服务端发来的消息',data.decode('utf-8'))

tcp_client.close()
