class Foo:
    def __getitem__(self, item):
        print('getitem',item)
        return self.__dict__[item]

    def __setitem__(self, key, value):
        print('setitem')
        self.__dict__[key]=value

    def __delitem__(self, key):
        print('delitem')
        self.__dict__.pop(key)

f1=Foo()
print(f1.__dict__)
# f1.name='egon'  #---->setattr-------->f1.__dict__['name']='egon'
f1['name']='egon'#--->setitem--------->f1.__dict__['name']='egon'
f1['age']=18

print('===>',f1.__dict__)

# del f1.name
# print(f1.__dict__)
#
# print(f1.age)
del f1['name']
print(f1.__dict__)

print(f1['age'])
raise S