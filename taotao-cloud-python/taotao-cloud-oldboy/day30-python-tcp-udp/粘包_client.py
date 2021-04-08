from socket import *
import time
ip_port=('127.0.0.1',8080)
back_log=5
buffer_size=1024

tcp_client=socket(AF_INET,SOCK_STREAM)
tcp_client.connect(ip_port)

tcp_client.send('hello'.encode('utf-8'))
tcp_client.send('world'.encode('utf-8'))
tcp_client.send('egon'.encode('utf-8'))


# time.sleep(1000)