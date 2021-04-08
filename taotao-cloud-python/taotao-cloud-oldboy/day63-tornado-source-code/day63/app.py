#!/usr/bin/env python
# -*- coding:utf-8 -*-
import tornado.ioloop
import tornado.web
class MainHandler(tornado.web.RequestHandler):
    def get(self):
        self.write("Hello, world")

class LoginHandler(tornado.web.RequestHandler):
    def get(self):
        # self.write("请登录")
        self.render("login.html")

    def post(self, *args, **kwargs):
        v = self.get_argument('username')
        print(v)
        self.redirect('/index.html')

settings = {
    'template_path': 'templates',
    'static_path': 'static',
    'static_url_prefix': '/ppp/',
}

# application对象中封装了：路由信息，配置信息
application = tornado.web.Application([
    (r"/login.html", LoginHandler),
    (r"/index.html", MainHandler),
],**settings)

# application.add_handlers('buy.oldboy.com',[
#     (r"/login.html", LoginHandler),
#     (r"/index.html", MainHandler),
# ])


if __name__ == "__main__":
    # 创建socket对象
    # sock = socket.socket()
    # inputs = [socket,]
    application.listen(8888)

    # 开启 r,w,e = select.select(inputs,)
    tornado.ioloop.IOLoop.instance().start()