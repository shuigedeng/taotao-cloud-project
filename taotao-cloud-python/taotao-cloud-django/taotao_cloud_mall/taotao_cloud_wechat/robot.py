# Create your views here.
from werobot import WeRoBot
from werobot.replies import ImageReply

APP_ID = os.environ.get('MP_APP_ID')
APP_SECRET = os.environ.get('MP_APP_SECRET')
APP_TOKEN = os.environ.get('MP_APP_TOKEN')
ENCODING_AES_KEY = os.environ.get('MP_ENCODING_AES_KEY')

robot = WeRoBot(token=APP_TOKEN)
robot.config["APP_ID"] = APP_ID
robot.config["APP_SECRET"] = APP_SECRET
robot.config['ENCODING_AES_KEY'] = ENCODING_AES_KEY

client = robot.client

button = [
    {
        "type": "click",
        "name": "今日歌曲",
        "key": "V1001_TODAY_MUSIC"
    },
    {
        "name": "菜单",
        "sub_button": [
            {
                "type": "view",
                "name": "搜索",
                "url": "http://www.soso.com/"
            },
            {
                "type": "view",
                "name": "视频",
                "url": "http://v.qq.com/"
            },
            {
                "type": "click",
                "name": "赞一下我们",
                "key": "V1001_GOOD"
            }]
    }]

match_rule = {
    "group_id": "2",
    "sex": "1",
    "country": "中国",
    "province": "广东",
    "city": "广州",
    "client_platform_type": "2",
    "language": "zh_CN"
}
# client.create_custom_menu(button, match_rule)


@robot.handler
def index(message):
    return 'Today is wonderful day!'


@robot.filter('image')
def media(message):
    media_id = client.upload_permanent_media('image', open(
        r'C:\Users\haojin\Desktop\favicon.png', 'rb'))['media_id']
    reply = ImageReply(message=message, media_id=media_id)
    return reply


@robot.text
def text(message):
    return '您发送了文本消息，内容为：' + message.content


@robot.image
def image(message):
    return '您发送了图片消息，图片为：' + message.img


@robot.link
def link(message):
    return '您发送了链接消息，链接为：' + message.url


@robot.location
def location(message):
    return '您发送了位置消息，位置为：' + message.label


@robot.voice
def voice(message):
    return '您发送了声音消息，media_id为：' + message.media_id


@robot.video
def video(message):
    return '您发送了视频消息，media_id为：' + message.media_id


@robot.subscribe
def subscribe(event):
    print('用户' + event.source + '关注了公众号')
    return '感谢关注voidking，您的ID为：' + event.source


@robot.unsubscribe
def unsubscribe(event):
    print('用户' + event.source + '取消了关注')
    return ''
