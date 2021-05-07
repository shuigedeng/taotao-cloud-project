import tornado.ioloop
import tornado.web
from tornado import gen
from tornado.concurrent import Future
import time


# 异步型的io请求
class MainHandler(tornado.web.RequestHandler):
    @gen.coroutine
    def get(self, *args, **kwargs):
        future = Future()
        tornado.ioloop.IOLoop.current().add_timeout(time.time() + 5, self.callback)
        yield future

    def callback(self):
        self.write("main handler")
        self.flush()


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