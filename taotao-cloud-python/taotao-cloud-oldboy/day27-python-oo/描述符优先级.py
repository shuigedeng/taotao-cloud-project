# class Foo:
#     def __get__(self, instance, owner):
#         print('===>get方法')
#     def __set__(self, instance, value):
#         print('===>set方法',instance,value)
#         # instance.__dict__['x']=value #b1.__dict__
#     def __delete__(self, instance):
#         print('===>delete方法')
#
#
# class Bar:
#     x=Foo() #在何地？

# print(Bar.x)

# Bar.x=1
# print(Bar.__dict__)
# print(Bar.x)

# print(Bar.__dict__)
# b1=Bar()
# b1.x   #get
# b1.x=1 # set
# del b1.x # delete


# b1=Bar()
# Bar.x=111111111111111111111111111111111111111
# b1.x
#
# del Bar.x
# b1.x



class Foo:
    def __get__(self, instance, owner):
        print('===>get方法')


    # def __delete__(self, instance):
    #     print('===>delete方法')


class Bar:
    x=Foo() #在何地？
    def  __getattr__(self, item):
        print('----->')

b1=Bar()
b1.x=1
print(b1.__dict__)
b1.xxxxxxxxxxxxxxxxxxxxxxx