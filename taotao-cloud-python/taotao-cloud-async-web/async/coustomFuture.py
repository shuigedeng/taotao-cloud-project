import tornado.ioloop
import tornado.web
from tornado import gen
from tornado.concurrent import Future

# 异步型的io请求
future = None


class MainHandler(tornado.web.RequestHandler):
    @gen.coroutine
    def get(self, *args, **kwargs):
        global future
        future = Future()
        future.add_done_callback(self.done)

        yield future

    def done(self):
        self.write("main handler")
        self.flush()


class IndexHandler(tornado.web.RequestHandler):
    def get(self, *args, **kwargs):
        global future
        future.set_result(None)
        self.write("index handler")


application = tornado.web.Application([
    (r'/main', MainHandler),
    (r'/index', IndexHandler)
])

if __name__ == '__main__':
    application.listen(8888)
    tornado.ioloop.IOLoop.instance().start()
