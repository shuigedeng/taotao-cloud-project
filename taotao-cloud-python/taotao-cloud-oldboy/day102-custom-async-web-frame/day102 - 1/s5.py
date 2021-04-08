import tornado.ioloop
import tornado.web
from tornado import gen
from tornado.concurrent import Future

future = None
class MainHandler(tornado.web.RequestHandler):
    @gen.coroutine
    def get(self):
        global future
        future = Future()
        future.add_done_callback(self.done)

        yield future

    def done(self, *args, **kwargs):
        self.write('Main')
        self.finish()

class IndexHandler(tornado.web.RequestHandler):
    def get(self):
        global future
        future.set_result(None)
        self.write("Index")

application = tornado.web.Application([
    (r"/main", MainHandler),
    (r"/index", IndexHandler),
])

if __name__ == "__main__":
    application.listen(8888)
    tornado.ioloop.IOLoop.instance().start()