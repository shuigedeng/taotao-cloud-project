"""
账号相关信息
"""

import tornado.ioloop
import tornado.web

class LoginHandler(tornado.web.RequestHandler):
    def get(self, *args, **kwargs):
        self.render('login.html',msg="")

    def post(self, *args, **kwargs):
        username = self.get_argument('user')
        password = self.get_argument('pwd')
        if username == "root" and password == '123':
            self.set_cookie('xxxxxx','oooooo')
            self.redirect('/home')
        else:
            self.render('login.html',msg="用户名或密码错误")
