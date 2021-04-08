import requests
post_dict = {
    "phone": '8615131255089',
    'password': 'woshiniba',
    'oneMonth': 1
}
response = requests.post(
    url="http://dig.chouti.com/login",
    data=post_dict
)
cookie_dict = response.cookies.get_dict()
print(cookie_dict)

response = requests.get(
    url='http://dig.chouti.com/profile',
    cookies=cookie_dict
)
