# # def func(start,end,a=0,b=0):
# #     if start == end:
# #         return a,b
# #     if start%3 == 0 and start%7 == 0:
# #         a +=1
# #         b += start
# #     ret = func(start+1,end,a,b)
# #     return ret
# # ret = func(1,7)
#
# l1 = [11, 22, 33]
# l2 = [22, 33, 44]
# set(l1)&set(l2)
#
#
# def f(name,age,sex):
#     pass
#
#
# f("jianwen",12,"male")
# f("jianwen",12,"male")
# f("jianwen",12,"male")
# f("jianwen",12,"male")
# f("jianwen",12,"male")
# f("jianwen",12,"male")
# f("jianwen",12,"male")
#
# def func(arg):
#    arg.append(55)
# li = [11, 22, 33, 44]
# func(li)
# print(li)
# li = func(li)
# print(li)
# print(li2)


# def func():
#    name = "seven"
#    def outer():
#        name = "eric"
#        def inner():
#            global name
#            name = "蒙逼了吧..."
#        print(name)
#    print(name)
#
# ret = func()
# print(ret)
# print(name)

# name = "root"
# def func():
#    name = "seven"
#
#    def outer():
#        name = "eric"
#        def inner():
#            global name
#            name = "蒙逼了吧..."
#        print(name)
#    o = outer()
#    print(o)
#    print(name)
#
# ret = func()
# print(ret)
# print(name)


# name = "root"
# def func():
#    name = "seven"
#    def outer():
#        name = "eric"
#        def inner():
#                global name
#                name = "..."
#        print(name)
#        inner()
#    o = outer()
#    print(o)
#    print(name)
#
# ret = func()
# print(ret)
# print(name)
# name = "root"
#
#
# def func():
#     name = "seven"
#     def outer():
#         name = "eric"
#         def inner():
#             nonlocal name
#             name = "蒙逼了吧..."
#             print(name)
#         inner()
#     o = outer()
#     print(o)
#     print(name)
#
# ret = func()
# print(ret)
# print(name)
#
#
# # n*(n-1)*....1
#
# def f(n):
#     if n==1:
#         return 1
#
#     return n*f(n-1)  #8*f(7)    7*f(6)  6*f(5).....  3*2    2*1
# f(8)
def func(x, y=0):
    y += 1
    if y == 5:
        return x + y
    x += y
    func(x, y)
    x += y
    return x
num = 1
result = func(num)
print(num)
print(result)

