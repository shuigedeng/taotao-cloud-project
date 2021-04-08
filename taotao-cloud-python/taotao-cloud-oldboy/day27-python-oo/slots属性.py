class Foo:
    __slots__=['name','age']  #{'name':None,'age':None}
    # __slots__='name' #{'name':None,'age':None}

f1=Foo()
# f1.name='egon'
# print(f1.name)

# f1.age=18  #--->setattr----->f1.__dict__['age']=18

# print(f1.__dict__)
print(Foo.__slots__)
print(f1.__slots__)
f1.name='egon'
f1.age=17
print(f1.name)
print(f1.age)
# f1.gender='male'


f2=Foo()
print(f2.__slots__)
f2.name='alex'
f2.age=18
print(f2.name)
print(f2.age)