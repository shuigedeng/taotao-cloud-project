# f=open('陈粒',encoding='utf-8')
# data=f.read()
# print(data)
# f.close()

# f=open('xxx')
# data=f.read()
# print(data)

#r w a
f=open('陈粒','r',encoding='utf-8')
# data=f.read()
# # print(data)
# print(f.readable())
# print('第1行',f.readline(),end='')
# print('第2行',f.readline())
# print('第3行',f.readline())
# # for i in range(1):
# #     pass
# print('第4行',f.readline())
# print('第5行',f.readline())
# print('第6行',f.readline())
# print('第7行',f.readline())

data=f.readlines()
print(data)
f.close()