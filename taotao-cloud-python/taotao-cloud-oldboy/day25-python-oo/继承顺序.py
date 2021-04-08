#coding:utf-8
class A:
    # def test(self):
    #     print('A')
    pass
class B(A):
    # def test(self):
    #     print('B')

    pass
class C(A):
    # def test(self):
    #     print('C')
    pass

class D(B):
    # def test(self):
    #     print('D')
    pass

class E(C):
    # def test(self):
    #     print('E')
    pass

class F(D,E):
    # def test(self):
    #     print('F')
    pass
f1=F()
f1.test()   #经典类：F->D->B->A-->E-->

# print(F.__mro__)

#F-->D->B-->E--->C--->A新式类
