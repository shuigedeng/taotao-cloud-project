# class Foo:
#     x=1
#     def __init__(self,y):
#         self.y=y
#
#     def __getattr__(self, item):
#         print('执行__getattr__')
#
# f1=Foo(10)
# print(f1.y)
# print(getattr(f1,'y'))   #len(str)--->str.__len__()
# f1.sssssssssssssssssssssssssssssssssssss


# class Foo:
#     x=1
#     def __init__(self,y):
#         self.y=y
#
#     def __delattr__(self, item):
#         print('删除操作__delattr__')
#
# f1=Foo(10)
# del f1.y
# del f1.x

#
# class Foo:
#     x=1
#     def __init__(self,y):
#         self.y=y
#
#     def __setattr__(self, key, value):
#         print('__setattr__执行')
#         # self.key=value
#         self.__dict__[key]=value
# f1=Foo(10)
# print(f1.__dict__)
# f1.z=2
# print(f1.__dict__)
# class Foo:
#     def __getattr__(self, item):
#         print('------------->')
#
# # print(Foo.__dict__)
# print(dir(Foo))
# f1=Foo()
#
# print(f1.x)  #只有在属性不存在时，会自动触发__getattr__
#
# del f1.x #删除属性时会触发＿ｄｅｌａｔｔｒ＿＿
#
# f1.y=10
# f1.x=3  # 设置属性的时候会触发——setattr———








class Foo:
    def __init__(self,name):
        self.name=name
    def __getattr__(self, item):
        print('你找的属性【%s】不存在' %item)
    def __setattr__(self, k,v):
        print('执行setattr',k,v)
        if type(v) is str:
            print('开始设置')
            # self.k=v #触发__setattr__
            self.__dict__[k]=v.upper()
        else:
            print('必须是字符串类型')
    def __delattr__(self, item):
        print('不允许删除属性【%s】' %item)
        # print('执行delattr',item)
        # del self.item
        # self.__dict__.pop(item)

f1=Foo('alex')
# f1.age=18 #触发__setattr__
# print(f1.__dict__)
# print(f1.name)
# print(f1.age)
# print(f1.gender)
# print(f1.slary)
print(f1.__dict__)
del f1.name
print(f1.__dict__)






