import  socket

phone=socket.socket(socket.AF_INET,socket.SOCK_STREAM)

phone.connect(('192.168.12.222',8001)) #拨通电话

phone.send('hello'.encode('utf-8')) #发消息
data=phone.recv(1024)
print('收到服务端的发来的消息：',data)