# f=open('xxx','r+',encoding='gbk')
# # data=f.read()
# # print(data)
# # f.write('123sb')
#
#
#
# f.write('sb')


#文件修改
# src_f=open('xxx','r',encoding='gbk')
# data=src_f.readlines()
# src_f.close()
#
# # for i in data:
# #     print(i)
# print(data)
# dst_f=open('xxx','w',encoding='gbk')
# # dst_f.writelines(data)
# dst_f.write(data[0])
# dst_f.close()

# with open('a.txt','w') as f:
#     f.write('1111\n')


# src_f=open('xxx','r',encoding='gbk')
# dst_f=open('xxx','w',encoding='gbk')
# with open('xxx','r',encoding='gbk') as src_f,\
#         open('xxx_new','w',encoding='gbk') as dst_f:
#     data=src_f.read()
#     dst_f.write(data)

f=open('a.txt')
print(f.encoding) #查看文件编码