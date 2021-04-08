class MyType(type):
    def __init__(self,a,b,c):
        print('元类的构造函数执行')
        # print(a)
        # print(b)
        # print(c)
    def __call__(self, *args, **kwargs):
        # print('=-======>')
        # print(self)
        # print(args,kwargs)
        obj=object.__new__(self) #object.__new__(Foo)-->f1
        self.__init__(obj,*args,**kwargs)  #Foo.__init__(f1,*arg,**kwargs)
        return obj
class Foo(metaclass=MyType): #Foo=MyType(Foo,'Foo',(),{})---》__init__
    def __init__(self,name):
        self.name=name #f1.name=name
# print(Foo)
# f1=Foo('alex')
# print(f1)

f1=Foo('alex')
print(f1)
print(f1.__dict__)