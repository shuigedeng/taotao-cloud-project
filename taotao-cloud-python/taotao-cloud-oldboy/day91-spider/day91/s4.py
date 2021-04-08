import requests

"""
r1 = requests.get('http://dig.chouti.com/')
r1_cookies = r1.cookies.get_dict()


post_dict = {
    "phone": '8615131255089',
    'password': 'woshiniba',
    'oneMonth': 1
}
r2 = requests.post(
    url="http://dig.chouti.com/login",
    data=post_dict,
    cookies=r1_cookies
)

r3 = requests.post(
    url='http://dig.chouti.com/link/vote?linksId=11832246',
    cookies={'gpsd': r1_cookies.get('gpsd')}
)
print(r3.text)
"""


import requests

session = requests.Session()

### 1、首先登陆任何页面，获取cookie
i1 = session.get(url="http://dig.chouti.com/help/service")

### 2、用户登陆，携带上一次的cookie，后台对cookie中的 gpsd 进行授权
i2 = session.post(
    url="http://dig.chouti.com/login",
    data={
        'phone': "8615131255089",
        'password': "woshiniba",
        'oneMonth': ""
    }
)

i3 = session.post(
    url="http://dig.chouti.com/link/vote?linksId=11837086",
)
print(i3.text)