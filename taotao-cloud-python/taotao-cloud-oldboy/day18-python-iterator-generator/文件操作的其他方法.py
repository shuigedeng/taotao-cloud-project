# f=open('a.txt','r+',encoding='gb2312')
# # data=f.read()
# # print(data)
# f.write('你好')

# f=open('b.txt','r+',encoding='latin-1')
# data=f.read()
# print(data)
# f.write('aaaaaaaaaaa')

# f=open('b.txt','r',encoding='utf-8',newline='') #读取文件中真正的换行符号
# f=open('b.txt','r+',encoding='utf-8',newline='') #读取文件中真正的换行符号

# print(f.closed)
# print(f.encoding)
# f.flush()
# print(f.readlines())

# print(f.tell())
# f.readline()
# print(f.tell())

# f.seek(1)
# print(f.tell())
# print(f.readlines())
# f.seek(3)
# print(f.tell())
# print(f.read())

# data=f.read(1)
# print(data)

# f.truncate(10)


# f.flush() #讲文件内容从内存刷到硬盘
#
# f.closed #文件如果关闭则返回True
#
# f.encoding #查看使用open打开文件的编码
# f.tell() #查看文件处理当前的光标位置
#
# f.seek(3) #从开头开始算，将光标移动到第三个字节
# f.truncate(10) #从开头开始算，将文件只保留从0-10个字节的内容，文件必须以写方式打开，但是w和w+除外
#
# f=open('d.txt','r',newline='')
#
# data=f.readline().encode('utf-8')
# print(data)
# print(f.tell())







# f=open('seek.txt','r',encoding='utf-8')
# print(f.tell())
# f.seek(10)
# print(f.tell())
# f.seek(3)
# print(f.tell())

# f=open('seek.txt','rb')
# print(f.tell())
# f.seek(10,1)
# print(f.tell())
# f.seek(3,1)
# print(f.tell())


# f=open('seek.txt','rb')
# print(f.tell())
# f.seek(-5,2)
# print(f.read())
# print(f.tell())
# f.seek(3,1)
# print(f.tell())









# f=open('日志文件','rb')
# data=f.readlines()
# print(data[-1].decode('utf-8'))

f=open('日志文件','rb')

# for i in f.readlines():
#     print(i)

#循环文件的推荐方式
# for i in f:
#     print(i)

for i in f:
    offs=-10
    while True:
        f.seek(offs,2)
        data=f.readlines()
        if len(data) > 1:
            print('文件的最后一行是%s' %(data[-1].decode('utf-8')))
            break
        offs*=2
