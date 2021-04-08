#!/usr/bin/env python
# -*- coding:utf-8 -*-
import time

import requests
from bs4 import BeautifulSoup

session = requests.Session()

i1 = session.get(
    url='https://www.zhihu.com/#signin',
    headers={
        'User-Agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.98 Safari/537.36',
    }
)

soup1 = BeautifulSoup(i1.text, 'html.parser')
xsrf_tag = soup1.find(name='input', attrs={'name': '_xsrf'})
xsrf = xsrf_tag.get('value')

current_time = time.time()
i2 = session.get(
    url='https://www.zhihu.com/captcha.gif',
    params={'r': current_time, 'type': 'login'},
    headers={
        'User-Agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.98 Safari/537.36',
    })

with open('zhihu.gif', 'wb') as f:
    f.write(i2.content)

captcha = input('请打开zhihu.gif文件，查看并输入验证码：')
form_data = {
    "_xsrf": xsrf,
    'password': 'xxooxxoo',
    "captcha": 'captcha',
    'email': '424662508@qq.com'
}
i3 = session.post(
    url='https://www.zhihu.com/login/email',
    data=form_data,
    headers={
        'User-Agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.98 Safari/537.36',
    }
)

i4 = session.get(
    url='https://www.zhihu.com/settings/profile',
    headers={
        'User-Agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.98 Safari/537.36',
    }
)

soup4 = BeautifulSoup(i4.text, 'html.parser')
tag = soup4.find(id='rename-section')
nick_name = tag.find('span',class_='name').string
print(nick_name)