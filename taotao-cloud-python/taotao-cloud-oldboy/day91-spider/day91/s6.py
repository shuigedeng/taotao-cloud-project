import requests
post_dict = {
    "phone": '8615131255089',
    'password': 'woshiniba',
    'oneMonth': 1
}
response = requests.post(
    url="http://dig.chouti.com/login",
    data=post_dict,
    proxys={
        'http': "http://4.19.128.5:8099"
    }
)