# f=open('test11.py','rb',encoding='utf-8') #b的方式不能指定编码
# f=open('test11.py','rb') #b的方式不能指定编码
# data=f.read()
# #'字符串'---------encode---------》bytes
# #bytes---------decode---------》'字符串'
# print(data)
# print(data.decode('utf-8'))
# f.close()


# f=open('test22.py','wb') #b的方式不能指定编码
# f.write(bytes('1111\n',encoding='utf-8'))
# f.write('杨件'.encode('utf-8'))

f=open('test22.py','ab') #b的方式不能指定编码
# f.write('杨件'.encode('utf-8'))

# open('a;ltxt','wt')


