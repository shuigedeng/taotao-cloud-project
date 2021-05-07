import tornado.ioloop
import tornado.web
from tornado import gen


# 异步型的io请求
class MainHandler(tornado.web.RequestHandler):
    @gen.coroutine
    def get(self, *args, **kwargs):
        import tornado.httpclient
        http = tornado.httpclient.AsyncHTTPClient()
        yield http.fetch("http://www.baidu.com", self.done)

    def done(self):
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