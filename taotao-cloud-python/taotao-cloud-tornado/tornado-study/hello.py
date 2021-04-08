import tornado.web
import tornado.ioloop
import tornado.httpserver


class HelloTorndaoHandler(tornado.web.RequestHandler):
    def get(self, *args, **kwargs):
        self.write('<h1>hell tornado</h1>')

    def post(self, *args, **kwargs):
        pass

if __name__ == '__main__':
    app = tornado.web.Application([
        (r'/', HelloTorndaoHandler)
    ])
    # app.listen(8000)
    http_server = tornado.httpserver.HTTPServer(app)
    http_server.listen(800)
    http_server.start(0)

    tornado.ioloop.IOLoop.current().start()