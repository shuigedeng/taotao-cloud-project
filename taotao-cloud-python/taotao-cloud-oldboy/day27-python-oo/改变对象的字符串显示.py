# l=list('hello')
#
# print(l)
# file=open('test.txt','w')
# print(file)

class Foo:
    def __init__(self,name,age):
        self.name=name
        self.age=age
    def __str__(self):
        return '名字是%s 年龄是%s' %(self.name,self.age)
#
# f1=Foo('egon',18)
# print(f1) #str(f1)--->f1.__str__()
#
# x=str(f1)
# print(x)
#
# y=f1.__str__()
# print(y)



class Foo:
    def __init__(self,name,age):
        self.name=name
        self.age=age
    # def __str__(self):
    #     return '折是str'
    def __repr__(self):
        return '名字是%s 年龄是%s' %(self.name,self.age)

f1=Foo('egon',19)
#repr(f1)---->f1.__repr__()
print(f1) #str（f1）---》f1.__str__()------>f1.__repr__()