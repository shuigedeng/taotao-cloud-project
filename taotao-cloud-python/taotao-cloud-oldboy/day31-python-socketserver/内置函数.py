# 三元运算
# a = 1
# result = "xiaoming" if a == 1 else "xiaogang"
# print(result)

# lambda 表达式

# def func(arg):
#     return arg + 1
# print(func(2))

# func = lambda *args : print(args)
# print(func(1,2,3))

# map
# li = [1,2,3,4,5]
# def func(s):
#      return s + 1
# ret = map(func,li)
# print(list(ret))

# print(list(map(lambda li : li + 1 ,li)))

# filter 过滤

# li = [22,33,44,55]
#
# def func(s):
#     if s > 33:
#         return s
# ret = filter(func,li)
# print(list(ret))
#
# print(list(filter(lambda s : s >33 ,li)))

# reduce

from functools import reduce

# li = ["a","l","e","x"]
# def func(a,b):
#     return a+b
# ret = reduce(func,li)
# print(ret)

li = [1,2,3,4,5]
print(reduce(lambda a,b : a +b ,li))


