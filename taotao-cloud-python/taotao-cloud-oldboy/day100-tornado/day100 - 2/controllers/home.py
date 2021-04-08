import tornado.ioloop
import tornado.web


class HomeHandler(tornado.web.RequestHandler):
    def get(self, *args, **kwargs):
        xx = self.get_secure_cookie('xxxxxx')
        if not xx:
            self.redirect('/login')
            return
        self.write('欢迎登陆')
