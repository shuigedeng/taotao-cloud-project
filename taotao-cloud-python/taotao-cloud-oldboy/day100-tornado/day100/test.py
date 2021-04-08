# class C:
#     def f1(self):
#         print('C')
# class A(C):
#     def f1(self):
#         print('A')
#         B.f1(self)
# class B:
#     def f1(self):
#         print('B')
# class Foo(A, B):
#     pass
#
# obj = Foo()
# obj.f1()

class Foo:
    def __contains__(self, item):
        print(item)
        return True

obj = Foo()

v = "x" in obj