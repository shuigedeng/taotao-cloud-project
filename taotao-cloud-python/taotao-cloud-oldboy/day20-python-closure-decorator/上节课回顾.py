# age=10
# res=True if age > 10 else False
#
# l=['a' for i in range(10)]
# g_l=('a' for i in range(10))



def test():
    for i in range(4):
        yield i
t=test()


# for i in t:
#     print(i)
#
# t1=(i for i in t)
# print(list(t1))

# t1=(i for i in t)
# t2=(i for i in t1)
# print(list(t1))
# print(list(t2))
