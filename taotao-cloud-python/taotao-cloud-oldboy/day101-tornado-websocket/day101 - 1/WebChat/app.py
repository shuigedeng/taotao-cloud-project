import tornado.web
import tornado.ioloop
import tornado.websocket
class IndexHandler(tornado.web.RequestHandler):

    def get(self, *args, **kwargs):
        self.render('index.html')

users = set()
class ChatHandler(tornado.websocket.WebSocketHandler):

    def open(self, *args, **kwargs):
        """
        客户端和服务端已经建立连接
        1. 连接
        2. 握手
        :param args:
        :param kwargs:
        :return:
        """
        users.add(self)

    def on_message(self, message):
        content = self.render_string('message.html',msg=message)
        for client in users:
            client.write_message(content)

    def on_close(self):
        """
        客户端主动关闭连接
        :return:
        """
        users.remove(self)


def run():
    settings = {
        'template_path': 'templates',
        'static_path': 'static',
    }
    application = tornado.web.Application([
        (r"/", IndexHandler),
        (r"/chat", ChatHandler),

    ], **settings)
    application.listen(8888)
    tornado.ioloop.IOLoop.instance().start()


if __name__ == "__main__":
    run()