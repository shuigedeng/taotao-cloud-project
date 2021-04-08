import tornado.web
import tornado.httpserver
import tornado.options
import tornado.ioloop


tornado.options.define('port', default=8000, type=int, help='this is this http server port')
tornado.options.define('hounify', default=[], type=str, help='slkflk')


class HelloTornadoOptions(tornado.web.RequestHandler):
    def get(self, *args, **kwargs):
        method = self.request.method


        self.write('hello tornado')

    def post(self, *args, **kwargs):
        pass


if __name__ == '__main__':
    tornado.options.parse_command_line()
    print(tornado.options.options.hounify)
    app = tornado.web.Application([
        (r'/', HelloTornadoOptions)
    ])
    http_server = tornado.httpserver.HTTPServer(app)
    http_server.listen(tornado.options.options.port)

    tornado.ioloop.IOLoop.current().start()