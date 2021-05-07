import tornado.ioloop
import tornado.web
import select


# 同步型的io请求
class MainHandler(tornado.web.RequestHandler):
    def get(self, *args, **kwargs):
        import time
        time.sleep(5)

        self.write("main handler")


class IndexHandler(tornado.web.RequestHandler):
    def get(self, *args, **kwargs):
        self.write("index handler")


application = tornado.web.Application([
    (r'/main', MainHandler),
    (r'/index', IndexHandler)
])

if __name__ == '__main__':
    application.listen(8888)
    tornado.ioloop.IOLoop.instance().start()


    epoll = select.epoll()