from socket import *
ip_port=('127.0.0.1',8080)
buffer_size=1024

udp_client=socket(AF_INET,SOCK_DGRAM) #数据报

udp_client.sendto(b'hello',ip_port)
udp_client.sendto(b'world',ip_port)
udp_client.sendto(b'egon',ip_port)
