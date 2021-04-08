class Typed:
    def __init__(self,key,expected_type):
        self.key=key
        self.expected_type=expected_type
    def __get__(self, instance, owner):
        print('get方法')
        # print('instance参数【%s】' %instance)
        # print('owner参数【%s】' %owner)
        return instance.__dict__[self.key]
    def __set__(self, instance, value):
        print('set方法')
        # print('instance参数【%s】' % instance)
        # print('value参数【%s】' % value)
        # print('====>',self)
        if not isinstance(value,self.expected_type):
            # print('你传入的类型不是字符串，错误')
            # return
            raise TypeError('%s 传入的类型不是%s' %(self.key,self.expected_type))
        instance.__dict__[self.key]=value
    def __delete__(self, instance):
        print('delete方法')
        # print('instance参数【%s】' % instance)
        instance.__dict__.pop(self.key)

def deco(**kwargs): #kwargs={'name':str,'age':int}
    def wrapper(obj): #obj=People
        for key,val in kwargs.items():#(('name',str),('age',int))
            setattr(obj,key,Typed(key,val))
            # setattr(People,'name',Typed('name',str)) #People.name=Typed('name',str)
        return obj
    return wrapper

@deco(name=str,age=int)  #@wrapper ===>People=wrapper(People)
class People:
    name='alex'
    # name=Typed('name',str)
    # age=Typed('age',int)
    def __init__(self,name,age,salary,gender,heigth):
        self.name=name
        self.age=age
        self.salary=salary
# p1=People('213',13.3,13.3,'x','y')
print(People.__dict__)

