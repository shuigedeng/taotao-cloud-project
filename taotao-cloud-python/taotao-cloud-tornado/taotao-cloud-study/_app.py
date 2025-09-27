import tornado.web
import tornado.ioloop
import tornado.options
import tornado.httpserver

from tornado.web import url, RequestHandler
from tornado.options import options

tornado.options.define('port', default=8000, type=int, help='port')

class IndexHandler(RequestHandler):
    def set_default_headers(self):
        print("执行了set_default_headers()")
        # 设置get与post方式的默认响应体格式为json
        self.set_header("Content-Type", "application/json; charset=UTF-8")
        # 设置一个名为itcast、值为python的header
        self.set_header("itcast", "python")

    def write_error(self, status_code, **kwargs):
        pass

    def prepare(self):
        pass

    def get_current_user(self):
        pass

    @tornado.web.authenticated
    def get(self, *args, **kwargs):
        name = self.get_argument('name', '123')
        print(name)

        print(self.request.uri)
        print(self.request.host)
        print(self.request.method)
        print(self.request.path)
        print(self.request.query)
        print(self.request.version)
        print(self.request.headers.get('User-Agent', None))
        print(self.request.body)
        print(self.request.remote_ip)

        # self.write('<a href="'+self.reverse_url('cpp_url')+'">cpp</a>')
        info = {
            'one': 1,
            'two': 2,
            'three': 3
        }
        import json
        # self.write(json.dumps(info))
        self.write(info)
        self.set_status(400)
        # self.set_header("Content-Type", "application/json; charset=UTF-8")
        self.send_error()
        self.redirect()

    def post(self, *args, **kwargs):
        pass

    def on_finish(self):
        pass


class SubjectHandler(RequestHandler):
    def initialize(self, name):
        self.name = name

    def get(self, *args, **kwargs):
        self.write(self.name)

if __name__ == '__main__':
    tornado.options.parse_command_line()
    app = tornado.web.Application([
        (r'/', IndexHandler),
        (r'/python', SubjectHandler, {'name': 'python'}),
        url(r'/cpp', SubjectHandler, {'name': 'cpp'}, name='cpp_url'),
    ], debug=True, login_url='/login')
    http_server = tornado.httpserver.HTTPServer(app)
    http_server.listen(options.port)

    tornado.ioloop.IOLoop.current().start()
