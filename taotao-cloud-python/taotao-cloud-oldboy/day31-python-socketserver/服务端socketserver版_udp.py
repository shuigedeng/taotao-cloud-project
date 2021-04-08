import socketserver

class MyServer(socketserver.BaseRequestHandler):
    def handle(self):
        print(self.request)
        print('收到客户端的消息是',self.request[0])
        self.request[1].sendto(self.request[0].upper(),self.client_address)


if __name__ == '__main__':
    s=socketserver.ThreadingUDPServer(('127.0.0.1',8080),MyServer) #多线程
    s.serve_forever()

