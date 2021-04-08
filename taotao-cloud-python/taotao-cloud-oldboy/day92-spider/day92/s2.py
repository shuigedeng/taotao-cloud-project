import requests
from bs4 import BeautifulSoup
# 1. 访问登陆页面，获取 authenticity_token
i1 = requests.get('https://github.com/login')
soup1 = BeautifulSoup(i1.text, features='html.parser')
tag = soup1.find(name='input', attrs={'name': 'authenticity_token'})
authenticity_token = tag.get('value')

# 获取第一次访问的cookie
c1 = i1.cookies.get_dict()
i1.close()

# 1. 携带authenticity_token和用户名密码等信息，发送用户验证
form_data = {
    "authenticity_token": authenticity_token,
    "utf8": "",
    "commit": "Sign in",
    "login": "wupeiqi@live.com",
    'password': 'xxoo'
}

i2 = requests.post('https://github.com/session', data=form_data, cookies=c1)

# 获取第二次的cookie
c2 = i2.cookies.get_dict()
# 获取第二次的cookie + 获取第二次的cookie
c1.update(c2)

i3 = requests.get('https://github.com/settings/repositories', cookies=c1)

soup3 = BeautifulSoup(i3.text, features='html.parser')
list_group = soup3.find(name='div', class_='listgroup')

from bs4.element import Tag

for child in list_group.children:
    if isinstance(child, Tag):
        project_tag = child.find(name='a', class_='mr-1')
        size_tag = child.find(name='small')
        temp = "项目:%s(%s); 项目路径:%s" % (project_tag.get('href'), size_tag.string, project_tag.string, )
        print(temp)