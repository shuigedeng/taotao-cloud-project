import tornado.ioloop
import tornado.web
from controllers.account import LoginHandler
from controllers.home import HomeHandler

class MainHandler(tornado.web.RequestHandler):
    def get(self):
        # self.write("Hello, world")
        # self.render("main.html")
        self.redirect('http://www.baidu.com')
import uimethods as mt
import uimodules as mm
from tornado import escape
settings = {
    "template_path": 'views',
    'cookie_secret':'asdfpojaksdfyknasdfklasdf',
    'ui_methods': mt,
    'ui_modules': mm,
    'static_path': 'static',
}
application = tornado.web.Application([
    (r"/index", MainHandler),
    (r"/login", LoginHandler),
    (r"/home", HomeHandler),
],**settings)

if __name__ == "__main__":
    application.listen(8888)
    tornado.ioloop.IOLoop.instance().start()