class Foo:
    def __get__(self, instance, owner):
        print('===>get方法')
    def __set__(self, instance, value):
        print('===>set方法',instance,value)
        instance.__dict__['x']=value #b1.__dict__
    def __delete__(self, instance):
        print('===>delete方法')


class Bar:
    x=Foo() #在何地？
    def __init__(self,n):
        self.x=n #b1.x=10
b1=Bar(10)
print(b1.__dict__)
b1.x=11111111111111111
print(b1.__dict__)

b1.y=11111111111111111111111111111111111111
print(b1.__dict__)
# print(Bar.__dict__)
#在何时？
# b1=Bar()
# b1.x
#
# b1.x=1
#
# del b1.x

# print(b1.x)
#
# b1.x=1
# print(b1.__dict__)
#
# del b1.x
