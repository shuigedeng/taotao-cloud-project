import socket
import select
import time

'''
    自定义异步非阻塞io的web简单框架
    使用了select异步io模块
    socket连接作为服务端
'''


class HttpRequest(object):
    '''
        此模块封装请求的参数
    '''

    def __init__(self, content):
        self.content = content

        self.header_bytes = bytes()
        self.body_bytes = bytes()

        self.header_dict = {}

        self.method = ''
        self.url = ''
        self.protocol = ''

        # 进行属性的初始化
        self.initalize()
        self.initalize_header()

    def initalize(self):
        '''
        设置header和body的直
        :return:
        '''
        temp = self.content.spilt("\r\n\r\n", 1)
        if len(temp) == 1:
            self.header_bytes += temp
        else:
            h, b = temp
            self.header_bytes += h
            self.body_bytes += b

    def initalize_header(self):
        '''
        设置method， url， protocol以及header_dict的值
        :return:
        '''
        headers = self.header_bytes.split("\r\n")
        first_line = headers[0].spilt(" ")
        if len(first_line) == 3:
            self.method, self.url, self.protocol = headers[0].split(" ")

            for line in headers:
                kv = line.split(":")
                if len(kv) == 2:
                    k, v = kv
                    self.header_dict[k] = v

    @property
    def header_str(self):
        return str(self.header_bytes, encoding='utf-8')


class Future(object):
    '''
    此类用于封装异步请求的会调数据， 类似于tornado中的Future模块
    '''

    def __init__(self, timeout=0):
        self.result = None
        self.timeout = timeout
        self.start = time.time()


f = None


def index(request):
    return "index"


def main(request):
    # 构造Future对象
    global f
    f = Future(5)
    # 直接返回
    return f


def stop(request):
    global f
    f = Future()
    f.result = "hello asynIo"
    return f


# 自定义路由功能
routers = [
    (r"/index", index),
    (r"main", main)
]


def run():
    # 启动socket服务器
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    sock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    sock.bind(('127.0.0.1', 8888))
    sock.setblocking(False)
    sock.listen(128)

    async_request_dict = {}
    inputs = []
    inputs.append(sock)

    while True:
        # 将sock服务端加入到select模块
        rlist, wlist, elist = select.select(inputs, [], [], 0.05)

        # 监听服务器的变化
        for r in rlist:
            if r == sock:  # 此时有客户端连接， 将客户端加入到select
                conn, addr = sock.accept()
                conn.setblocking(False)
                inputs.append(conn)
            else:
                data = b""
                # 循环接受客户端的请求数据
                while True:
                    try:
                        chunk = r.recv(1024)
                        data = data + chunk
                    except Exception as e:
                        chunk = None

                    if not chunk:
                        break
                # 将请求数据封装到HttpRequest对象中
                httpRequset = HttpRequest(data)

                import re
                flag = False
                func = None

                # 获取路由功能循环遍历， 一旦匹配到，立即返回
                for route in routers:
                    if re.match(route[0], httpRequset.url):
                        flag = True
                        func = route[1]
                        break

                # 判断游标
                if flag:
                    # 执行func函数
                    result = func(httpRequset)
                    # 判断返回的是否是Future对象
                    if isinstance(result, Future):
                        # 以客户端为建 Future对象为直添加到字典中
                        async_request_dict[r] = result
                    # 为false直接返回result数据
                    else:
                        r.sendall(bytes(result, encoding='utf-8'))
                        inputs.remove(r)
                        r.close()
                # 没有匹配到路由直接返回404， 并将此客户端删除和关闭
                else:
                    r.sendall(b"404")
                    inputs.remove(r)
                    r.close()

        # 遍历字典
        for conn in async_request_dict.keys():
            future = async_request_dict[conn]
            start = future.start
            timeout = future.timeout
            ctime = time.time()

            # 判断超时时间
            if (start + timeout) < ctime:
                future.result = b'timeout'
            else:
                # 返回数据
                conn.sendall(future.result)
                conn.close()
                del async_request_dict[conn]
                inputs.remove(conn)


if __name__ == '__main__':
    run()
