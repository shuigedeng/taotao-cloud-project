from bs4 import BeautifulSoup

html_doc = """
<html><head><title>The Dormouse's story</title></head>
<body>
    <div><a href='http://www.cunzhang.com'>剥夺老师<p>asdf</p></a></div>
    <a id='i1'>刘志超</a>
    <div>
        <p>asdf</p>
    </div>
    <p>asdfffffffffff</p>
</body>
</html>
"""

soup = BeautifulSoup(html_doc, features="html.parser")

tag = soup.find('a')
v = tag.unwrap()
print(soup)
# from bs4.element import Tag
# obj1 = Tag(name='div', attrs={'id': 'it'})
# obj1.string = '我是一个新来的'
#
# tag = soup.find('a')
# v = tag.wrap(obj1)
# print(soup)


# tag = soup.find('body')
# tag.append(soup.find('a'))
# print(soup)

# from bs4.element import Tag
# obj = Tag(name='i', attrs={'id': 'it'})
# obj.string = '我是一个新来的'
# tag = soup.find('body')
# # tag.insert_before(obj)
# tag.insert_after(obj)
# print(soup)


# tag = soup.find('p',recursive=False)
# print(tag)
# tag = soup.find('body').find('p',recursive=False)
# print(tag)

# tag = soup.find('a')
# v = tag.get_text()
# print(v)

# 属性操作
# tag = soup.find('a')
# tag.attrs['lover'] = '物理老师'
# del tag.attrs['href']
# print(soup)

# children: 儿子
# 标签和内容
# from bs4.element import Tag
# tags = soup.find('body').children
# for tag in tags:
#     if type(tag) == Tag:
#         print(tag,type(tag))
#     else:
#         print('文本....')

# tags = soup.find('body').descendants
# print(list(tags))


# tag = soup.find('body')
# # 把对象转换成字节类型
# print(tag.encode_contents())
# # 把对象转换成字符串类型
# print(tag.decode_contents())
# # print(str(tag))


