# class Foo:
#     @property
#     def AAA(self):
#         print('get的时候运行我啊')
#
#     @AAA.setter
#     def AAA(self,val):
#         print('set的时候运行我啊',val)
#     @AAA.deleter
#     def AAA(self):
#         print('del的时候运行我啊')
# #只有在属性AAA定义property后才能定义AAA.setter,AAA.deleter
# f1=Foo()
# f1.AAA
# f1.AAA='aaa'
# del f1.AAA


class Foo:

    def get_AAA(self):
        print('get的时候运行我啊')
    def set_AAA(self,val):
        print('set的时候运行我啊',val)
    def del_AAA(self):
        print('del的时候运行我啊')

    AAA=property(get_AAA,set_AAA,del_AAA)
#只有在属性AAA定义property后才能定义AAA.setter,AAA.deleter
f1=Foo()
f1.AAA
f1.AAA='aaa'
del f1.AAA