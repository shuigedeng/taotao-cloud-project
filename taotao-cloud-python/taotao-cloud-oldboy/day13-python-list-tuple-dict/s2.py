#!/usr/bin/env python
# -*- coding:utf-8 -*-
# l = [11,22,33,44,55,66,77,88,99,90]
# result = {}
# # {'k1': [77, 88, 99, 90], 'k2': [11, 22, 33, 44, 55]}
#
# for item in l:
#     if item < 66:
#         # result.update({"k1": item}) # {'k1':11}
#         # {'k1': [11,22]}
#         if "k1" not in result:
#             result['k1'] = [item, ]    # {'k1': [11,]}
#         else:
#             result['k1'].append(item)
#     elif item > 66:
#         if "k2" not in result:
#             result['k2'] = [item, ]    # {'k1': [11,]}
#         else:
#             result['k2'].append(item)




# for i in l:
#     if i < 66:
#         a.append(i)
#     if i > 66:
#         b.append(i)
# print({'k1':b,'k2':a})

# 有两个列表
# l1	=	[11,22,33]
# l2	=	[22,33,44]
# a.	 获取内容相同的元素列表
# b.	 获取 l1	 中有， l2 中没有的元素列表
# c.	 获取 l2	 中有， l1 中没有的元素列表
# d.	 获取 l1	 和 l2	 中内容都不同的元素
# l1	=	[11,22,33]
# l2	=	[22,33,44]
# for i in l2:
#     if i not in l1:
#         print(i)
# for i in l1:
#     if i not in l2:
#         print(i)
# count =0
#
# for i in range(1, 9):
#     for v in range(1, 9):
#         if i !=v:
#             count += 1
# # count = count -8
# print(count)
#
# count =0
# ls = [3,5,8,9]
# for i in range(0, len(ls)):
#     for v in range(0, len(ls)):
#         if i !=v:
#             count += 1
# # count = count -8
# print(count)

#99乘法表
# for i in range(1,10):
#     string = ""
#     for j in range(1,i+1):
#         string +=str(j) + " * "+str(i) + " = " +str(i*j)+"\t"
#     print(string)

# print("aaa",end="")
# print("bbb")

# for i in range(1,10):
#     for j in range(1,i+1):
#         print(str(j) + " * "+str(i) + " = " +str(i*j)+"\t",end="")
#     print("\n",end="")

# for i in range(1,10):
#     for j in range(1,i+1):
#

# \n
# print('alex', end='SB')
# print('alex')
# print('alex','haifeng','gangniang',sep='搞')
# 这里面的数字两个组合 多少种不同样的，数字不重复的

# li = [1,2,3,4]
# l =len(li)
# for i in range(0,l-1):
#     for v in range(i+1,l):
#         print(li[i],li[v])
# for x in range(1,100//5):
#     for y in range(1,100//3):
#         for z in range(1,100):
#             if x + y + z == 100 and 5*x + 3*y + z/3 == 100:
#                 print(x,y,z)


# li = ['alex','eric',123]
# li[2] = str(li[2])
#
#
# v = "_".join(li)
# print(v)
# tu = ('alex', 'eric', 'rain')
# print(len(tu))
# print(tu[2])
# print(tu[1:])
# #step 为正
# print(tu[1:90])
# for elem in tu:
#     print(elem)
# for idx in range(len(tu)):
#     print(idx)

# for idx, elem in enumerate(tu, 10):
#     print(idx, elem)
# tu =(
#     "alex",
#     [
#         11,
#         22,
#         {
#             "k1":'v1',
#             "k2":["age","name"],
#             "k3":(11,22,33)
#         },
#         44
#     ]
# )
# tu[1][2]["k2"].append("")


# nums = [2,7,11,15,1,8,7]
# a =[]
# for i in range(len(nums)):
#     for j in range(len(nums)):
#         if nums[i] + nums[j] ==9:
#             a.append((i,j,))
# print(a)

# li = ["sdsdsd"]
# print (len(li))
# li.append("dsad")
# li.insert(0,"dsad")
# li.remove("eric")
# v = li.pop(1)
# print(li,v)
#
# v = li.reverse()
# for i in range(len(li)):
#     print(i)
# for i,q in enumerate(li,100):
#     print(i,q)
# for i in li:
#     print(i)

# user_list = [
# ]
# for i in range(1,302):
#     temp = {'name': 'alex'+str(i), 'email': 'alex@lve.com' + str(i), 'pwd': 'pwd'+str(i)}
#     user_list.append(temp)
#
# while True:
#     # 每页显示10条数据
#     s = input("请输入1,2,3-31页码：")
#     s = int(s)
#     # user_list[0,10]  1
#     # user_list[10,20]  2
#     # user_list[20,30]  3
#     start = (s-1) * 10
#     end = s * 10
#     result = user_list[start: end]
#     for item in result:
#         print(item,type(item))