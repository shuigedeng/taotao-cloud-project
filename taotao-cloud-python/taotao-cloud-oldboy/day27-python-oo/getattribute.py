# class Foo:
#     def __init__(self,x):
#         self.x=x
#
#     def __getattr__(self, item):
#         print('执行的是我')
#         # return self.__dict__[item]
#
# f1=Foo(10)
# # print(f1.x)
# f1.xxxxxx #不存在的属性访问，触发__getattr__



class Foo:
    def __init__(self,x):
        self.x=x

    def __getattr__(self, item):
        print('执行的是getattr')
        # return self.__dict__[item]
    # def __getattribute__(self, item):
    #     print('执行的是getattribute')
    #     raise AttributeError('抛出异常了')
        # raise TabError('xxxxxx')
f1=Foo(10)
# f1.x
f1.xxxxxx #不存在的属性访问，触发__getattr__

