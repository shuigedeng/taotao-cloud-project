#map
# l=[1,2,3,4,5]
#
# print(list(map(str,l)))

#reduce
# from functools import reduce
# l=[1,2,3,4,5]
# # reduce(func,l)
# res=reduce(lambda x,y:x+y,l,3)
# print(res)

#filter
# name=['alex_sb','linhaifeng']
# res=filter(lambda x:not x.endswith('sb'),name)
# print(res)
# print(list(res))

#文件操作
# f=open('test11.py','w',encoding='utf-8')
# f.write('1111\n')
# f.write('1111\n')
# f.write('1111\n')
# f.close()

# f=open('test11.py','a',encoding='utf-8')
# f.write('这是a模式的内容')
# f.close()

# f=open('test11.py','r+',encoding='utf-8')
# f.write('hello')

