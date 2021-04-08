import tornado.ioloop
import tornado.web
from tornado import gen
from tornado.concurrent import Future
import time

class MainHandler(tornado.web.RequestHandler):
    @gen.coroutine
    def get(self):
        future = Future()
        # 特殊的形式等待5s
        tornado.ioloop.IOLoop.current().add_timeout(time.time() + 5, self.done)
        yield future
    def done(self, *args, **kwargs):
        self.write('Main')
        self.finish()


class IndexHandler(tornado.web.RequestHandler):
    def get(self):
        self.write("Index")
application = tornado.web.Application([
    (r"/main", MainHandler),
    (r"/index", IndexHandler),
])

if __name__ == "__main__":
    application.listen(8888)
    tornado.ioloop.IOLoop.instance().start()