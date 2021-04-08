'''
Python开发【第五篇】：Python基础之杂货铺
    http://www.cnblogs.com/wupeiqi/articles/5484747.html

    http://www.cnblogs.com/linhaifeng/articles/7580428.html
'''


# x='hello'
# # print(dir(x))
# iter_test=x.__iter__()
#
# print(iter_test)
# print(iter_test.__next__())
# print(iter_test.__next__())
# print(iter_test.__next__())
# print(iter_test.__next__())
# print(iter_test.__next__())
# print(iter_test.__next__())

####################################

l=[1,2,3]
l.__iter__()
for i in l:  #i_l=l.__iter_()  i_l.__next__()
    print(i)

index=0
while index < len(l):
    print(l[index])
    index+=1


iter_l=l.__iter__() #遵循迭代器协议，生成可迭代对象
print(iter_l.__next__())
print(iter_l.__next__())

for i in l:
    print(i)

####################################

#s={1,2,3}

# for i in s:
#     print(i)
# iter_s=s.__iter__()
# print(iter_s)
# print(iter_s.__next__())
# print(iter_s.__next__())
# print(iter_s.__next__())
# print(iter_s.__next__())
####################################

# dic={'a':1,'b':2}
# iter_d=dic.__iter__()
# print(iter_d.__next__())

####################################

# f=open('test.txt','r+')
# # for i in f:
# iter_f=f.__iter__()
# print(iter_f)
# print(iter_f.__next__(),end='')
# print(iter_f.__next__(),end='')

####################################

# l=[1,2,3,4,5]
# diedai_l=l.__iter__()
# while True:
#     try:
#         print(diedai_l.__next__())
#     except StopIteration:
#         # print('迭代完毕了,循环终止了')
#         break

####################################

#l=['die','erzi','sunzi','chongsunzi']

#iter_l=l.__iter__()
#print(iter_l)
# print(iter_l.__next__())
# print(iter_l.__next__())
# print(iter_l.__next__())
# print(iter_l.__next__())
# print(iter_l.__next__())
# print(next(iter_l)) #next()---->iter_l.__next__()

####################################
# def test():
#     yield 1
#     yield 2
#     yield 3
# g=test()
# print('来自函数',g)
# print(g.__next__())
# print(g.__next__())

####################################

#三元表达式
# name='alex'
# name='linhaifeng'
# res='SB' if name == 'alex' else '帅哥'
# print(res)

####################################

#列表解析
# egg_list=[]
# for i in range(10):
#     egg_list.append('鸡蛋%s' %i)
# print(egg_list)

#l=['鸡蛋%s' %i for i in range(10)]
# l1=['鸡蛋%s' %i for i in range(10) if i > 5 ]
# # l1=['鸡蛋%s' %i for i in range(10) if i > 5 else i] #没有四元表达式
# l2=['鸡蛋%s' %i for i in range(10) if i < 5] #没有四元表达式

#print(l)
# print(l1)
# print(l2)

# laomuji=('鸡蛋%s' %i for i in range(10)) #生成器表达式
# print(laomuji)
# print(laomuji.__next__())
# print(laomuji.__next__())
# print(next(laomuji))
# print(next(laomuji))
# print(next(laomuji))
# print(next(laomuji))
# print(next(laomuji))
# print(next(laomuji))
# print(next(laomuji))
# print(next(laomuji))
# print(next(laomuji))

#l=[1,2,3,34]
# map(func,l)

# print(sum(l))
# print(sum())
# print(sum(i for i in range(10000000000000)))

