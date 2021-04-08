import tornado.ioloop
import tornado.web


class MainHandler(tornado.web.RequestHandler):
    def get(self):
        import requests
        requests.get('http://www.google.com')
        self.write('xxxxx')

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