#!/usr/bin/env python
# -*- coding:utf-8 -*-

# v = "李杰"
# for item in v:
#     print(item)
####################################################################################################
# str
# name = "alex"

# list  # 类，列表
# li = [1, 12, 9, "age", ["石振文", ["19", 10], "庞麦郎"], "alex", True]  # 通过list类创建的对象，li
# list 类
# list类的一个对象


#######################################灰魔法: list类中提供的方法 #######################################

# li = [11, 22, 33, 22, 44]
# 参数
# 1. 原来值最后追加
# 对象.方法(..)   # li对象调用append方法
# li.append(5)
# li.append("alex")
# li.append([1234,2323])
# print(li)

# 2 清空列表
# li.clear()
# print(li)

# 3 拷贝，浅拷贝
# v = li.copy()
# print(v)

# 4. 计算元素出现的次数
# v = li.count(22)
# print(v)

# 5. 扩展原列表，参数：可迭代对象
# li = [11, 22, 33, 22, 44]
# li.append([9898,"不得了"])
# [11, 22, 33, 22, 44, [9898, '不得了']]

# li.extend([9898,"不得了"])
# for i in [9898,"不得了"]:
#     li.append(i)
# [11, 22, 33, 22, 44, 9898, '不得了']
#
# li.extend("不得了")
# print(li)

# 6. 根据值获取当前值索引位置（左边优先）
# li = [11, 22, 33, 22, 44]
# v= li.index(22)
# print(v)

# 7. 在指定索引位置插入元素
# li = [11, 22, 33, 22, 44]
# li.insert(0,99)
# print(li)

# 8、 删除某个值(1.指定索引；2. 默认最后一个)，并获取删除的值
# li = [11, 22, 33, 22, 44]
# v = li.pop()
# print(li)
# print(v)

# li = [11, 22, 33, 22, 44]
# v = li.pop(1)
# print(li)
# print(v)

# 9. 删除列表中的指定值，左边优先
# li = [11, 22, 33, 22, 44]
# li.remove(22)
# print(li)
# PS: pop remove del li[0]    del li[7:9]   clear

# 10 将当前列表进行翻转
# li = [11, 22, 33, 22, 44]
# li.reverse()
# print(li)

# 11 列表的排序
# li = [11,44, 22, 33, 22]
# li.sort()
# li.sort(reverse=True)
# print(li)

### 欠
# cmp
# key
# sorted

####################################### 深灰魔法 #######################################
# 1. 列表格式
# 2. 列表中可以嵌套任何类型
# 中括号括起来
# ，分割每个元素
# 列表中的元素可以是 数字，字符串,列表，布尔值..所有的都能放进去
# “集合”，内部放置任何东西
"""
# 3.
# 索引取值
print(li[3])

# 4 切片，切片结果也是列表
print(li[3:-1])

# 5 for循环
# while循环
for item in li:
    print(item)
"""
# 列表元素，可以被修改

# li = [1, 12, 9, "age", ["石振文", ["19", 10], "庞麦郎"], "alex", True]

############## 6 索引
# 修改
# li[1] = 120
# print(li)
# li[1] = [11,22,33,44]
# print(li)

# 删除,第一种方式
# del li[1]
# print(li)
############## 7 切片
# 修改
# li[1:3] = [120,90]
# print(li)
# 删除
# del li[2:6]
# print(li)

# 8 in 操作
# li = [1, 12, 9, "age", ["石振文", ["19", 10], "庞麦郎"], "alex", True]
# v1 = "石振文" in li
# print(v1)
# v2 = "age" in li
# print(v2)
###### 列表中的元素，

# 9 操作
# li = [1, 12, 9, "age", ["石振文", ["19", 10], "庞麦郎"], "alex", True]
# li[4][1][0]
# [1]

# li = [1, 12, 9, "age", ["石振文", ["19", 10], "庞麦郎"], "alex", True]

# s = "pouaskdfauspdfiajsdkfj"
# s = 123
# a = "123"
# int(a)
# a = 123
# str(a)

# 10 转换
# 字符串转换列表   li =  list("asdfasdfasdf"), 内部使用for循环
# s = "pouaskdfauspdfiajsdkfj"
# new_li = list(s)
# print(new_li)

# 列表转换成字符串，
# 需要自己写for循环一个一个处理： 既有数字又有字符串
# li = [11,22,33,"123","alex"]
# # r = str(li) # '[11,22,33,"123","alex"]'
# # print(r)
# s = ""
# for i in li:
#     s = s + str(i)
# print(s)
# 直接使用字符串join方法：列表中的元素只有字符串
# li = ["123","alex"]
# v = "".join(li)
# print(v)

### 补充：字符串创建后，不可修改
# v = "alex"
# v = v.replace('l','el')
# print(v)

# li = [11,22,33,44]
# li[0]
# li[0] = 999

# s = "alex"
# li[0]
# s[0] = "E"

# li = [11,22,33,44]
# print(li)
# print(li)
# print(li)
# print(li)
# print(li)
# print(li)
# print(li)
# print(li)
# 列表，有序；元素可以被修改

# 列表
# list
# li = [111,22,33,44]


####################################################################################################

# 元组，元素不可被修改，不能被增加或者删除
# tuple
# tu = (11,22,33,44)
# tu.count(22),获取指定元素在元组中出现的次数
# tu.index(22)

####################################### 深灰魔法 #######################################
# 1. 书写格式
# tu = (111,"alex",(11,22),[(33,44)],True,33,44,)
# 一般写元组的时候，推荐在最后加入 ,
# 元素不可被修改，不能被增加或者删除

# 2. 索引
# v = tu[0]
# print(v)

# 3. 切片
# v = tu[0:2]
# print(v)

# 4. 可以被for循环，可迭代对象
# for item in tu:
#     print(item)

# 5. 转换
# s = "asdfasdf0"
# li = ["asdf","asdfasdf"]
# tu = ("asdf","asdf")
#
# v = tuple(s)
# print(v)

# v = tuple(li)
# print(v)

# v = list(tu)
# print(v)

# v = "_".join(tu)
# print(v)

# li = ["asdf","asdfasdf"]
# li.extend((11,22,33,))
# print(li)

# 6.元组的一级元素不可修改/删除/增加
# tu = (111,"alex",(11,22),[(33,44)],True,33,44,)
# # 元组，有序。
# # v = tu[3][0][0]
# # print(v)
# # v=tu[3]
# # print(v)
# tu[3][0] = 567
# print(tu)

####################################################################################################
# 字典
# dict
# dict
# dic = {
#     "k1": 'v1',
#     "k2": 'v2'
# }

# 1 根据序列，创建字典，并指定统一的值
# v = dict.fromkeys(["k1",123,"999"],123)
# print(v)

# 2 根据Key获取值，key不存在时，可以指定默认值（None）
# v = dic['k11111']
# print(v)
# v = dic.get('k1',111111)
# print(v)

# 3 删除并获取值
# dic = {
#     "k1": 'v1',
#     "k2": 'v2'
# }
# v = dic.pop('k1',90)
# print(dic,v)
# k,v = dic.popitem()
# print(dic,k,v)

# 4 设置值，
# 已存在，不设置，获取当前key对应的值
# 不存在，设置，获取当前key对应的值
# dic = {
#     "k1": 'v1',
#     "k2": 'v2'
# }
# v = dic.setdefault('k1111','123')
# print(dic,v)

# 5 更新
# dic = {
#     "k1": 'v1',
#     "k2": 'v2'
# }
# dic.update({'k1': '111111','k3': 123})
# print(dic)
# dic.update(k1=123,k3=345,k5="asdf")
# print(dic)

# 6 keys()  7 values()   8 items()   get   update
##########



# 1、基本机构
# info = {
#     "k1": "v1", # 键值对
#     "k2": "v2"
# }

#### 2 字典的value可以是任何值
# info = {
#     "k1": 18,
#     "k2": True,
#     "k3": [
#         11,
#         [],
#         (),
#         22,
#         33,
#         {
#             'kk1': 'vv1',
#             'kk2': 'vv2',
#             'kk3': (11,22),
#         }
#     ],
#     "k4": (11,22,33,44)
# }
# print(info)

####  3 布尔值(1,0)、列表、字典不能作为字典的key
# info ={
#     1: 'asdf',
#     "k1": 'asdf',
#     True: "123",
#     # [11,22]: 123
#     (11,22): 123,
#     # {'k1':'v1'}: 123
#
# }
# print(info)

# 4 字典无序

# info = {
#     "k1": 18,
#     "k2": True,
#     "k3": [
#         11,
#         [],
#         (),
#         22,
#         33,
#         {
#             'kk1': 'vv1',
#             'kk2': 'vv2',
#             'kk3': (11,22),
#         }
#     ],
#     "k4": (11,22,33,44)
# }
# print(info)

# 5、索引方式找到指定元素
# info = {
#     "k1": 18,
#     2: True,
#     "k3": [
#         11,
#         [],
#         (),
#         22,
#         33,
#         {
#             'kk1': 'vv1',
#             'kk2': 'vv2',
#             'kk3': (11,22),
#         }
#     ],
#     "k4": (11,22,33,44)
# }
# # v = info['k1']
# # print(v)
# # v = info[2]
# # print(v)
# v = info['k3'][5]['kk3'][0]
# print(v)

# 6 字典支持 del 删除
# info = {
#     "k1": 18,
#     2: True,
#     "k3": [
#         11,
#         [],
#         (),
#         22,
#         33,
#         {
#             'kk1': 'vv1',
#             'kk2': 'vv2',
#             'kk3': (11,22),
#         }
#     ],
#     "k4": (11,22,33,44)
# }
# del info['k1']
#
# del info['k3'][5]['kk1']
# print(info)

# 7 for循环
# dict
# info = {
#     "k1": 18,
#     2: True,
#     "k3": [
#         11,
#         [],
#         (),
#         22,
#         33,
#         {
#             'kk1': 'vv1',
#             'kk2': 'vv2',
#             'kk3': (11,22),
#         }
#     ],
#     "k4": (11,22,33,44)
# }
# for item in info:
#     print(item)
#
# for item in info.keys():
#     print(item)

# for item in info.values():
#     print(item)

# for item in info.keys():
#     print(item,info[item])

# for k,v in info.items():
#     print(k,v)

# True 1  False 0
# info ={
#     "k1": 'asdf',
#     True: "123",
#     # [11,22]: 123
#     (11,22): 123,
#     # {'k1':' v1'}: 123
#
# }
# print(info)

#######################　整理 #################

# 一、数字
# int(..)

# 二、字符串
# replace/find/join/strip/startswith/split/upper/lower/format
# tempalte = "i am {name}, age : {age}"
# # v = tempalte.format(name='alex',age=19)
# v = tempalte.format(**{"name": 'alex','age': 19})
# print(v)

# 三、列表
# append、extend、insert
# 索引、切片、循环

# 四、元组
# 忽略
# 索引、切片、循环         以及元素不能被修改

# 五、字典
# get/update/keys/values/items
# for,索引

# dic = {
#     "k1": 'v1'
# }

# v = "k1" in dic
# print(v)

# v = "v1" in dic.values()
# print(v)

# 六、布尔值
# 0 1
# bool(...)
# None ""  () []  {} 0 ==> False



























