import tornado.ioloop
import tornado.web
from controllers.account import LoginHandler
from controllers.home import HomeHandler

class MainHandler(tornado.web.RequestHandler):
    def get(self):
        # self.write("Hello, world")
        # self.render("main.html")
        self.redirect('http://www.baidu.com')

settings = {
    "template_path": 'views',
}
application = tornado.web.Application([
    (r"/index", MainHandler),
    (r"/login", LoginHandler),
    (r"/home", HomeHandler),
],**settings)

if __name__ == "__main__":
    application.listen(8888)
    tornado.ioloop.IOLoop.instance().start()