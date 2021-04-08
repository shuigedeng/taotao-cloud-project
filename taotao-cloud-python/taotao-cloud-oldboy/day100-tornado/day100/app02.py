import tornado.ioloop
import tornado.web
from controllers.account import LoginHandler
from controllers.home import HomeHandler

import time
import hashlib

class Cache(object):
    """
    将session保存在内存
    """
    def __init__(self):
        self.container = {}

    def __contains__(self, item):
        return item in self.container

    def initial(self,random_str):
        self.container[random_str] = {}

    def get(self,random_str,key):
        return self.container[random_str].get(key)

    def set(self,random_str,key,value):
        self.container[random_str][key] = value

    def delete(self,random_str,key):
        del self.container[random_str][key]

    def open(self):
        pass

    def close(self):
        pass

    def clear(self,random_str):
        del self.container[random_str]

class Memcache(object):
    def __init__(self):
        pass

    def get(self,key):
        pass

    def set(self,key,value):
        pass

    def delete(self,key):
        pass

    def open(self):
        pass

    def close(self):
        pass

P = Cache

class Session(object):
    def __init__(self,handler):
        self.handler = handler
        self.random_str = None
        self.ppp = P()
        self.ppp.open()
        # 去用户请求信息中获取session_id，如果没有，新用户
        client_random_str = self.handler.get_cookie('session_id')
        if not client_random_str:
            "新用户"
            self.random_str = self.create_random_str()
            container[self.random_str] = {}
        else:
            if client_random_str in self.ppp:
                "老用户"
                self.random_str = client_random_str
            else:
                "非法用户"
                self.random_str = self.create_random_str()
                self.ppp.initial(self.random_str)
        ctime = time.time()
        self.handler.set_cookie('session_id',self.random_str,expires=ctime+1800)
        self.ppp.close()

    def create_random_str(self):
        v = str(time.time())
        m = hashlib.md5()
        m.update(bytes(v,encoding='utf-8'))
        return m.hexdigest()

    def __setitem__(self, key, value):
        self.ppp.open()
        self.ppp.set(self.random_str,key,value)
        self.ppp.close()
    def __getitem__(self, key):
        self.ppp.open()
        v = self.ppp.get(self.random_str,key)
        self.ppp.close()
        return v
    def __delitem__(self, key):
        self.ppp.open()
        self.ppp.delete(self.random_str,key)
        self.ppp.close()

    def clear(self):
        self.ppp.open()
        self.ppp.clear(self.random_str)
        self.ppp.close()

class Foo(object):
    def initialize(self):
        # self是MainHandler对象
        self.session = Session(self)
        super(Foo,self).initialize()

class HomeHandler(Foo,tornado.web.RequestHandler):

    def get(self):
        user = self.session['uuuuu']
        if not  user:
            self.redirect("http://www.oldboyedu.com")
        else:
            self.write(user)

class LoginHandler(Foo,tornado.web.RequestHandler):

    def get(self):
        self.session['uuuuu'] = 'root'
        self.redirect('/home')

class TestHandler(tornado.web.RequestHandler):
    def get(self):
        self.set_cookie('k1', 'vvv', expires=time.time()+20)

class ShowHandler(tornado.web.RequestHandler):
    def get(self):
        self.write(self.get_cookie('k1'))



application = tornado.web.Application([
    (r"/login", LoginHandler),
    (r"/home", HomeHandler),
    (r"/test", TestHandler),
    (r"/show", ShowHandler),
])

if __name__ == "__main__":
    application.listen(9999)
    tornado.ioloop.IOLoop.instance().start()