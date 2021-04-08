from django.shortcuts import render,HttpResponse
import requests
import time
import re
import json


CTIME = None
QCODE = None
TIP = 1
ticket_dict ={}



def login(request):
    global CTIME
    CTIME = time.time()
    response = requests.get(
        url ='https://login.wx.qq.com/jslogin?appid=wx782c26e4c19acffb&fun=new&lang=zh_CN&_=%s' % CTIME
    )
    v = re.findall('uuid = "(.*)";',response.text)
    global QCODE
    QCODE = v[0]
    return render(request,'login.html',{'qcode':QCODE})

def check_login(request):
    global TIP
    ret = {'code': 408,'data': None}
    r1 = requests.get(
        url="https://login.wx.qq.com/cgi-bin/mmwebwx-bin/login?loginicon=true&uuid=%s&tip=%s&r=95982085&_=%s" %(QCODE,TIP,CTIME,)
    )
    if 'window.code=408' in  r1.text:
        print('无人扫码')
        return HttpResponse(json.dumps(ret))
    elif 'window.code=201' in  r1.text:
        ret['code'] = 201
        avatar = re.findall("window.userAvatar = '(.*)';", r1.text)[0]
        ret['data'] = avatar
        TIP = 0
        return HttpResponse(json.dumps(ret))
    elif 'window.code=200' in  r1.text:
        # 用户点击确认登录，
        """
        window.code=200;
        window.redirect_uri="https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxnewloginpage?ticket=AYKeKS9YQnNcteZCfLeTlzv7@qrticket_0&uuid=QZA2_kDzdw==&lang=zh_CN&scan=1494553432";
        window.redirect_uri="https://wx2.qq.com/cgi-bin/mmwebwx-bin/webwxnewloginpage?ticket=AYKeKS9YQnNcteZCfLeTlzv7@qrticket_0&uuid=QZA2_kDzdw==&lang=zh_CN&scan=1494553432";
        """

        redirect_uri = re.findall('window.redirect_uri="(.*)";', r1.text)[0]
        redirect_uri = redirect_uri + "&fun=new&version=v2"

        # 获取凭证
        r2 = requests.get(url=redirect_uri)
        from bs4 import BeautifulSoup
        soup = BeautifulSoup(r2.text,'html.parser')
        for tag in soup.find('error').children:
            ticket_dict[tag.name] = tag.get_text()
        print(ticket_dict)


        # 获取用户信息
        # https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxinit?r=88828930&lang=zh_CN&pass_ticket=uBfBw5um5Zor97ihMqdFprf4kqjecz8q0VRdevL%252BMg7Ozij4NvnpZCevYQX5jhO0
        # get_user_info_data = {
        #     'BaseRequest': {
        #         'DeviceID': "e402310790089148",
        #         'Sid':ticket_dict['wxsid'],
        #         'Uin':ticket_dict['wxuin'],
        #         'Skey':ticket_dict['skey'],
        #     }
        # }
        # get_user_info_url = "https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxinit?r=88828930&lang=zh_CN&pass_ticket=" +ticket_dict['pass_ticket']
        # r3 = requests.post(
        #     url=get_user_info_url,
        #     json=get_user_info_data
        # )
        # r3.encoding = 'utf-8'
        # user_init_dict = json.loads(r3.text)
        # print(user_init_dict)
        ret['code'] = 200
        return HttpResponse(json.dumps(ret))