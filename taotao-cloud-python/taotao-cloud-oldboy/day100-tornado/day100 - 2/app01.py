import tornado.ioloop
import tornado.web
from controllers.account import LoginHandler
from controllers.home import HomeHandler

class Foo(tornado.web.RequestHandler):
    def initialize(self):
        self.A = 123
        self.set_cookie(ag)
        super(Foo,self).initialize()

class MainHandler(Foo):

    def get(self):
        print(self.A)
        self.write("Hello, world")

application = tornado.web.Application([
    (r"/index", MainHandler),
])

if __name__ == "__main__":
    application.listen(9999)
    tornado.ioloop.IOLoop.instance().start()