# def test(x):
#     print('===>',x)
#
# test('alex')
# test(111111)


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

class People:
    name=Typed('name',str) #t1.__set__()  self.__set__()
    age=Typed('age',int) #t1.__set__()  self.__set__()
    def __init__(self,name,age,salary):
        self.name=name
        self.age=age
        self.salary=salary

# p1=People('alex','13',13.3)
p1=People(213,13,13.3)

# p1=People('alex',13,13.3)
# print(p1.__dict__)
# p1=People(213,13,13.3)
# print(p1.__dict__)
# print(p1.__dict__)
# print(p1.name)

# print(p1.__dict__)
# p1.name='egon'
# print(p1.__dict__)


# print(p1.__dict__)
# del p1.name
# print(p1.__dict__)

# print(p1)

# print(p1.name)
# p1.name='egon'
# print(p1.name)
# print(p1.__dict__)



