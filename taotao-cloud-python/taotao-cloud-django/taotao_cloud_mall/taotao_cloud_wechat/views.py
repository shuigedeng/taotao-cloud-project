import json
import re
import time

import requests
from django.shortcuts import render, HttpResponse

# 记录当前时间戳
CURRENT_TIME = None

# 记录返回的二维码的后缀
QCODE = None

# 记录长连接的次数
TIPS = 1

# 获取登录成功的cookies
SUCCESS_LONGIN_COOKIES = {}

# 保存票据的cookies
TICKET_COOKIES_DICT = {}

# 保存票据的信息
TICKET_DICT = {}

# 保存用户的基本信息
USER_INIT_DATA = {}

BASE_URL = "http://wx.qq.com"

BASE_SYNC_URL = "https://webpush.weixin.qq.com"


def login(request):
    '''登录界面、获取二维码'''
    base_qcode_url = "https://login.wx.qq.com/jslogin?appid=wx782c26e4c19acffb&redirect_uri=https%3A%2F%2Fwx.qq.com" \
                     "%2Fcgi-bin%2Fmmwebwx-bin%2Fwebwxnewloginpage&fun=new&lang=zh_CN&_={0}"
    global CURRENT_TIME
    CURRENT_TIME = str(time.time())
    # 构造二维码的请求URL
    send_qcode_url = base_qcode_url.format(CURRENT_TIME)
    # 通过requests模块发送请求
    reponse = requests.get(send_qcode_url)

    # 通过返回值获取二维码的后缀
    global QCODE
    QCODE = re.findall('uuid = "(.*)";', reponse.text)[0]

    return render(request, "wechat/login.html", {"code": QCODE})


def long_polling(request):
    '''等待用户的扫吗、并返回成功与否'''

    # 构造一个返回数据结构
    # 408 用户未扫吗
    # 201 用户已扫码，未提交
    # 200 用户已经扫吗、已提交
    response_message = {"status": 408, "data": None}

    try:
        global TIPS
        base_login_url = "https://login.wx.qq.com/cgi-bin/mmwebwx-bin/login?loginicon=true&uuid={0}&tip={1}&r=1870606180&_={2}"

        # 微信登录的url
        login_url = base_login_url.format(QCODE, TIPS, CURRENT_TIME)
        print(login_url)
        # 获取微信登录的信息
        login_reponse = requests.get(login_url)
        print(login_reponse)

        if "window.code=201" in login_reponse.text:
            TIPS = 0
            response_message['status'] = 201
            response_message['data'] = \
                re.findall(r"userAvatar = '(.*)';", login_reponse.text)[0]
        elif "window.code=200" in login_reponse.text:  # 登录成功
            # 扫码点击确认后，获取cookie
            global SUCCESS_LONGIN_COOKIES
            SUCCESS_LONGIN_COOKIES.update(login_reponse.cookies.get_dict())
            # 获取跳转的url
            redirect_uri = \
                re.findall('redirect_uri="(.*)";', login_reponse.text)[0]

            global BASE_URL
            global BASE_SYNC_URL
            if redirect_uri.startswith('https://wx2.qq.com'):
                BASE_URL = 'https://wx2.qq.com'
                BASE_SYNC_URL = 'https://webpush.wx2.qq.com'
            else:
                BASE_URL = "http://wx.qq.com"
                BASE_SYNC_URL = "https://webpush.weixin.qq.com"

            redirect_uri += '&fun=new&version=v2&lang=zh_CN'

            # 通过redirect_uri获取 票据、Cookie、返回值
            reponse_ticekt = requests.get(redirect_uri,
                                          cookies=SUCCESS_LONGIN_COOKIES)

            global TICKET_COOKIES_DICT
            global TICKET_DICT
            TICKET_COOKIES_DICT.update(reponse_ticekt.cookies.get_dict())

            from bs4 import BeautifulSoup
            soup = BeautifulSoup(reponse_ticekt.text, 'html.parser')
            for tag in soup.find():
                TICKET_DICT[tag.name] = tag.string
            response_message['status'] = 200

    except Exception as e:
        print(e)
    return HttpResponse(json.dumps(response_message))


def index(request):
    '''跳转成功显示用户的信息 初始化用户基本信息'''
    # 用户的基本信息请求路径
    print(TICKET_DICT)
    user_init_url = '%s/cgi-bin/mmwebwx-bin/webwxinit?pass_ticket=%s&r=%s' % (
        BASE_URL, TICKET_DICT['pass_ticket'], int(time.time()))
    print(user_init_url)

    # 提交的表单数据
    form_data = {
        'BaseRequest': {
            'DeviceID': 'e531777446530354',
            'Sid': TICKET_DICT['wxsid'],
            'Skey': TICKET_DICT['skey'],
            'Uin': TICKET_DICT['wxuin']
        }
    }

    # 提交的cookies
    all_cookie_dict = {}
    all_cookie_dict.update(SUCCESS_LONGIN_COOKIES)
    all_cookie_dict.update(TICKET_COOKIES_DICT)

    reponse_init = requests.post(user_init_url, json=form_data,
                                 cookies=all_cookie_dict)
    reponse_init.encoding = 'utf-8'
    print(reponse_init)
    # 获得用户的初始化数据
    user_init_data = json.loads(reponse_init.text)
    USER_INIT_DATA.update(user_init_data)

    return render(request, 'wechat/index.html', {"data": user_init_data})


def contact_list(request):
    """
    获取联系人列表
    :param request:
    :return:
    """
    base_url = "{0}/cgi-bin/mmwebwx-bin/webwxgetcontact?lang=zh_CN&pass_ticket={1}&r={2}&seq=0&skey={3}"
    url = base_url.format(BASE_URL, TICKET_DICT['pass_ticket'],
                          str(time.time()), TICKET_DICT['skey'])

    all_cookie_dict = {}
    all_cookie_dict.update(SUCCESS_LONGIN_COOKIES)
    all_cookie_dict.update(TICKET_COOKIES_DICT)

    response = requests.get(url, cookies=all_cookie_dict)
    response.encoding = 'utf-8'
    contact_list_dict = json.loads(response.text)
    return render(request, 'wechat/contact_list.html', {'obj': contact_list_dict})


def send_msg(request):
    # 当前用户的id
    from_user_id = USER_INIT_DATA['User']['UserName']
    # 要发送别的用户的id
    to_user_id = request.POST.get('user_id')
    # 发送的消息
    msg = request.POST.get('user_msg')

    # 发送的url
    send_url = BASE_URL + "/cgi-bin/mmwebwx-bin/webwxsendmsg?lang=zh_CN&pass_ticket=" + \
               TICKET_DICT['pass_ticket']
    # 发送的表单数据
    form_data = {
        'BaseRequest': {
            'DeviceID': 'e531777446530354',
            'Sid': TICKET_DICT['wxsid'],
            'Skey': TICKET_DICT['skey'],
            'Uin': TICKET_DICT['wxuin']
        },
        'Msg': {
            "ClientMsgId": str(time.time()),
            "Content": '%(content)s',
            "FromUserName": from_user_id,
            "LocalID": str(time.time()),
            "ToUserName": to_user_id,
            "Type": 1
        },
        'Scene': 0
    }

    # 字符串
    form_data_str = json.dumps(form_data)
    # 进行格式化
    form_data_str = form_data_str % {'content': msg}
    # 转换成字节
    form_data_bytes = bytes(form_data_str, encoding='utf-8')

    all_cookie_dict = {}
    all_cookie_dict.update(SUCCESS_LONGIN_COOKIES)
    all_cookie_dict.update(TICKET_COOKIES_DICT)

    response = requests.post(send_url, data=form_data_bytes,
                             cookies=all_cookie_dict, headers={
            'Content-Type': 'application/json'})

    return HttpResponse('ok')


def get_msg(request):
    sync_url = BASE_SYNC_URL + "/cgi-bin/mmwebwx-bin/synccheck"

    sync_data_list = []
    for item in USER_INIT_DATA['SyncKey']['List']:
        temp = "%s_%s" % (item['Key'], item['Val'])
        sync_data_list.append(temp)

    sync_data_str = "|".join(sync_data_list)
    nid = int(time.time())

    sync_dict = {
        "r": nid,
        "skey": TICKET_DICT['skey'],
        "sid": TICKET_DICT['wxsid'],
        "uin": TICKET_DICT['wxuin'],
        "deviceid": "e531777446530354",
        "synckey": sync_data_str
    }

    all_cookie = {}
    all_cookie.update(SUCCESS_LONGIN_COOKIES)
    all_cookie.update(TICKET_COOKIES_DICT)

    response_sync = requests.get(sync_url, params=sync_dict, cookies=all_cookie)

    if 'selector:"2"' in response_sync.text:
        fetch_msg_url = "%s/cgi-bin/mmwebwx-bin/webwxsync?sid=%s&skey=%s&lang=zh_CN&pass_ticket=%s" % (
            BASE_URL, TICKET_DICT['wxsid'], TICKET_DICT['skey'],
            TICKET_DICT['pass_ticket'])

        form_data = {
            'BaseRequest': {
                'DeviceID': 'e531777446530354',
                'Sid': TICKET_DICT['wxsid'],
                'Skey': TICKET_DICT['skey'],
                'Uin': TICKET_DICT['wxuin']
            },
            'SyncKey': USER_INIT_DATA['SyncKey'],
            'rr': str(time.time())
        }

        response_fetch_msg = requests.post(fetch_msg_url, json=form_data)

        response_fetch_msg.encoding = 'utf-8'
        res_fetch_msg_dict = json.loads(response_fetch_msg.text)

        USER_INIT_DATA['SyncKey'] = res_fetch_msg_dict['SyncKey']

        for item in res_fetch_msg_dict['AddMsgList']:
            print(item['Content'], ":::::", item['FromUserName'], "---->",
                  item['ToUserName'], )
    return HttpResponse('ok')
