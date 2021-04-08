import requests



# requests.post(
#     url='xxx',
#     files={
#         'f1': open('s1.py','rb'),
#         'f2': ('ssssss1.py',open('s1.py','rb'))
#     }
# )

def param_auth():
    from requests.auth import HTTPBasicAuth, HTTPDigestAuth

    # ret = requests.get('https://api.github.com/user', auth=HTTPBasicAuth('wupeiqi', 'sdfasdfasdf'))

    # ret = requests.get('https://api.github.com/user',headers={'Authorization':_basic_auth_str(self.username, self.password)})

    print(ret.text)

    # ret = requests.get('http://192.168.1.1',
    # auth=HTTPBasicAuth('admin', 'admin'))
    # ret.encoding = 'gbk'
    # print(ret.text)

    # ret = requests.get('http://httpbin.org/digest-auth/auth/user/pass', auth=HTTPDigestAuth('user', 'pass'))
    # print(ret)
    #