import tornado.web
import tornado.options
import tornado.ioloop
import tornado.httpserver
import tornado.gen
import hashlib
import xmltodict
import time
import datetime
import json
import os

from tornado.web import RequestHandler
from tornado.options import options, define
from tornado.httpclient import AsyncHTTPClient, HTTPRequest

define('port', default=8000, type=int, help='server port')

# 微信公众号
WEBCHAT_TOKEN = 'taotao'

# 微信的ppid
WECHAT_APPID = ''

# 微信的appsecret
WECHAT_APPSECRET = ''


class WebChatHandler(RequestHandler):
    """对接微信服务器"""

    def prepare(self):
        signature = self.get_argument('signature')
        timestamp = self.get_argument('timestamp')
        nonce = self.get_argument('nonce')

        # 将token、 timesamp，nonce进行排序
        tmp = [WEBCHAT_TOKEN, timestamp, nonce]
        tmp.sort()

        # 讲排序后的字符串拼接
        tmp = ''.join(tmp)

        # 将拼接后的字符串进行加密
        real_singature = hashlib.sha1(tmp).hexsdigest()

        # 判断签名验证是否成功
        if real_singature != signature:
            self.send_error(403)

            # 将此程序推送到公网服务器、运行启动
            # 在测试号中先填写借口配置信息URL：http://公网地址/webchat8000, Token为程序中定义的WEBCHAT_TOKEN
            # 注意由于此程序启动的是8000端口，所以应该在公网服务器安装nginx进行反向映射。nginx启动80端口映射到8000端口
            # 点击提交 出现配置成功

    def get(self, *args, **kwargs):
        echostr = self.get_argument('echostr')
        self.write(echostr)

    def post(self, *args, **kwargs):
        """接受和发送消息"""

        xml_data = self.request.body
        dict_data = xmltodict.parse(xml_data)
        msg_type = dict_data['xml']['MsgType']

        # 构造一个返回数据
        rsp_data = {
            "xml": {
                "ToUserName": dict_data['xml']['FromUserName'],
                "FromUserName": dict_data['xml']['ToUserName'],
                'CreateTime': int(time.time()),
                'MsgType': 'text',
                'Content': '',
            }
        }

        """
        <xml>
            <ToUserName><![CDATA[toUser]]></ToUserName>
            <FromUserName><![CDATA[fromUser]]></FromUserName> 
            <CreateTime>1348831860</CreateTime>
            <MsgType><![CDATA[text]]></MsgType>
            <Content><![CDATA[this is a test]]></Content>
            <MsgId>1234567890123456</MsgId>
        </xml>
        """

        if msg_type == 'text':
            """接受和发送文本消息"""
            context = dict_data['xml']['Context']
            rsp_data['xml']['Content'] = context
        elif "voice" == msg_type:
            """接受和发送语音消息"""
            rsp_data['xml']['Content'] = dict_data['xml'].get('Recognition', '未识别')
        elif msg_type == 'event':
            """接受和发送订阅消息 关注/取消关注事件"""
            if dict_data['xml']['Event'] == 'subscribe':
                if 'EventKey' in dict_data['xml']:
                    event_key = dict_data['xml']['EventKey']
                    scene_id = event_key[8:]
                    rsp_data['xml']['Content'] = '您来啦！小二在这恭候多时%s！' % scene_id
                else:
                    rsp_data['xml']['Content'] = '您来啦！小二在这恭候多时！'
            elif dict_data['xml']['Event'] == 'SCAN':
                scene_id = dict_data['xml']['EventKey']
                rsp_data['xml']['Content'] = '您扫描的是%s！' % scene_id
            else:
                rsp_data = None
        else:
            rsp_data['xml']['Content'] = 'I LOVE YOU'

        print(xmltodict.unparse(rsp_data))
        self.write(xmltodict.unparse(rsp_data))


class AccessToken(object):
    """微信借口调用token"""

    _access_token = {
        'token': '',
        'updatetime': datetime.datetime.now()
    }

    @classmethod
    @tornado.gen.coroutine
    def get_access_token(cls):
        """获取token的值"""

        if not cls._access_token["token"] or (
                    datetime.datetime.now() - cls._access_token["updatetime"]).seconds >= 6600:
            yield cls.update_access_token()
        raise tornado.gen.Return(cls._access_token["token"])

    @classmethod
    @tornado.gen.coroutine
    def update_access_token(cls):
        """更新token的值"""

        client = AsyncHTTPClient()
        url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s" % (
            WECHAT_APPID, WECHAT_APPSECRET)
        resp = yield client.fetch(url)

        print(resp.body)
        ret = json.loads(resp.body)
        token = ret.get("access_token")
        if token:
            cls._access_token["token"] = token
            cls._access_token['updatetime'] = datetime.datetime.now()


class QRCodeHandler(RequestHandler):
    """获取二维码"""

    @tornado.gen.coroutine
    def get(self):
        access_token = yield AccessToken.get_access_token()
        print(access_token)
        url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=%s" % access_token
        scene_id = self.get_argument("scene_id")
        req_body = '{"expire_seconds": 7200, "action_name": "QR_SCENE", "action_info": {"scene": {"scene_id": %s}}}' % scene_id
        client = AsyncHTTPClient()
        req = HTTPRequest(url, method="POST", body=req_body)
        resp = yield client.fetch(req)
        if "errcode" in resp.body:
            self.write("error")
        else:
            resp_data = json.loads(resp.body)
            ticket = resp_data['ticket']
            self.write('<img src="https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=%s">' % ticket)


class ProfileHandler(RequestHandler):
    @tornado.gen.coroutine
    def get(self):
        code = self.get_argument("code")
        if not code:
            self.write("您未授权，无法获取您的信息！")
            return
        client = AsyncHTTPClient()
        url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type" \
              "=authorization_code" % (WECHAT_APPID, WECHAT_APPSECRET, code)
        resp = yield client.fetch(url)
        ret = json.loads(resp.body)
        access_token = ret.get("access_token")
        if access_token:
            openid = ret.get("openid")
            url = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN" % (access_token, openid)
            resp = yield client.fetch(url)
            ret = json.loads(resp.body)
            if ret.get("errcode"):
                self.write(ret.get("errmsg", "获取个人信息错误"))
            else:
                self.render("user.html", user=ret)
        else:
            self.write(ret.get("errmsg", "获取access_token错误"))


def main():
    tornado.options.parse_command_line()
    app = tornado.web.Application([
        (r'/webchat8000', WebChatHandler),
        (r'/qrcode', QRCodeHandler),
        (r'/profile', ProfileHandler)
    ], template_path=os.path.join(os.path.dirname(__file__), 'template'))
    server = tornado.httpserver.HTTPServer(app)
    server.listen(options.port)
    tornado.ioloop.IOLoop.current().start()

if __name__ == "__main__":
    main()
