import test as obj

print(obj)

print(hasattr(obj,'say_hi'))
print(hasattr(obj,'say_hisssssssssssssssssssssssssssssssssssssssssss'))

if hasattr(obj,'say_hi'):
    func=getattr(obj,'say_hi')
    func()
else:
    print('其他的逻辑')

x=111
y=222



# import fanshe as obj1
import sys
obj1=sys.modules[__name__]

print('===>',hasattr(obj1,'x'))




