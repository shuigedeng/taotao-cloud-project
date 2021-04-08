class Foo:
    pass

class Bar(Foo):
    pass

b1=Bar()
print(isinstance(b1,Bar))
print(isinstance(b1,Foo))
print(type(b1))
#
# l=list('hello')
# print(type(l))