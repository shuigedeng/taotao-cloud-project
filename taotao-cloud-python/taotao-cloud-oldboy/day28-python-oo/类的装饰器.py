# def deco(func):
#     print('==========')
#     return func
#
# # @deco       #test=deco(test)
# # def test():
# #     print('test函数运行')
# # test()
#
# @deco #Foo=deco(Foo)
# class Foo:
#     pass




def deco(obj):
    print('==========',obj)
    obj.x=1
    obj.y=2
    obj.z=3
    return obj
# @deco #Foo=deco(Foo)
# class Foo:
#     pass
#
# print(Foo.__dict__)

#一切皆对象
# # @deco #test=deco(test)
# def test():
#     print('test函数')
# test.x=1
# test.y=1
# print(test.__dict__)

