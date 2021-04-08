#!/usr/bin/env python
# -*- coding:utf-8 -*-

# num = 9 // 2
# print(num)
#



# name = "郑建文"

# "郑建文"   字符串
# "郑"   字符
# "建文"   建文 子字符串，子序列
# ctrl + ?

# name = "郑建文"

# if "建文" in name:
#     print('OK')
# else:
#     print('Error')

# if "文1" not in name:
# if 1 == 1:
# if 2 > 2:
# if True:
#     print('1')
# else:
#     print('2')

# v = 1 == 2
# print(v)



# user = "alex"
# pwd = "123"
#
# v = user == 'alex' and pwd == "12" or 1 == 1 and pwd == "99854" and 1==2
# print(v)

# 数字比作猎人
# num = 123
# v = num.upper()
# print(v)

"""
# 字符串：女巫
name1 = "shizhengwen"
v1 = name1.upper()
print(v1)

name2 = "LaiYing"
v2 = name2.upper()
print(v2)
"""

# 整形， int
# Python3里，1234123123123123123123123123123123123
# Python2里，1234123112
# 长整形，long
# Python2里，12341231124321342342  long
# ======== Python3 =========
# 整形   int
# Python3里，1234123123123123123123123123123123123
# a = 111
# a.bit_length()
# 字符串  str
# s1 = "alex"
# s2 = "root"
# s1.title()
# s1.upper()
# s1.startswith('xx')
# 列表  list

# 元祖  tuple

# 字典  dict

# 布尔值 bool


# a = "123a"
# print(type(a),a)
#
# b = int(a)
# print(type(b),b)

# num = "b"
# v = int(num, base=16)
# print(v)

# age = 5
# 1  1
# 2  10
# 3  11
# 4  100
# 5  101
# 当前数字的二进制，至少用n位表示
# r = age.bit_length()
# print(r)

# test = "aLex"
# 首字母大写
# v = test.capitalize()
# print(v)

# 所有变小写，casefold更牛逼，很多未知的对相应变小写
# v1 = test.casefold()
# print(v1)
# v2 = test.lower()
# print(v2)

# 设置宽度，并将内容居中
# 20 代指总长度
# *  空白未知填充，一个字符，可有可无
# v = test.center(20,"中")
# print(v)

# 去字符串中寻找，寻找子序列的出现次数
# test = "aLexalexr"
# v = test.count('ex')
# print(v)

# test = "aLexalexr"
# v = test.count('ex',5,6)
# print(v)

# 欠
# encode
# decode

# 以什么什么结尾
# 以什么什么开始
# test = "alex"
# v = test.endswith('ex')
# v = test.startswith('ex')
# print(v)

# 欠
# test = "12345678\t9"
# v = test.expandtabs(6)
# print(v,len(v))

# 从开始往后找，找到第一个之后，获取其未知
# > 或 >=
# test = "alexalex"
# 未找到 -1
# v = test.find('ex')
# print(v)

# index找不到，报错   忽略
# test = "alexalex"
# v = test.index('8')
# print(v)


# 格式化，将一个字符串中的占位符替换为指定的值
# test = 'i am {name}, age {a}'
# print(test)
# v = test.format(name='alex',a=19)
# print(v)

# test = 'i am {0}, age {1}'
# print(test)
# v = test.format('alex',19)
# print(v)

# 格式化，传入的值 {"name": 'alex', "a": 19}
# test = 'i am {name}, age {a}'
# v1 = test.format(name='df',a=10)
# v2 = test.format_map({"name": 'alex', "a": 19})

# 字符串中是否只包含 字母和数字
# test = "123"
# v = test.isalnum()
# print(v)

test = "hello"
