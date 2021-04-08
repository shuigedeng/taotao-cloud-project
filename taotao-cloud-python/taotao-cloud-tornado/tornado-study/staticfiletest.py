import tornado.web
import tornado.httpserver
import tornado.options
import tornado.ioloop
import os


tornado.options.define('port', default=8000, type=int, help='this is this http server port')
tornado.options.define('hounify', default=[], type=str, help='slkflk')


class HelloTornadoOptions(tornado.web.RequestHandler):
    def get(self, *args, **kwargs):
        # self.write('hello tornado')
        self.render('index.html', a1='nihao ya saliya')

    def post(self, *args, **kwargs):
        pass

if __name__ == '__main__':
    tornado.options.parse_command_line()

    app = tornado.web.Application(
        # [(r'/', HelloTornadoOptions)],
        [
            (r'/', HelloTornadoOptions),
            (r'^/view/(.*)$', tornado.web.StaticFileHandler, {"path": os.path.join(os.path.dirname(__file__), "statics/html")}),
            # (r'^/()$', tornado.web.StaticFileHandler, {"path": os.path.join(os.path.dirname(__file__), "statics/html"), "default_filename": "index.html"}),
        ],
        debug=True,
        template_path=os.path.join(os.path.dirname(__file__), "templates"),
        static_path=os.path.join(os.path.dirname(__file__), 'statics'),
        autoescape=None,
    )

    http_server = tornado.httpserver.HTTPServer(app)
    http_server.listen(tornado.options.options.port)

    tornado.ioloop.IOLoop.current().start()