f=open('陈粒1','w',encoding='utf8')
# f.read()
f.write('11111111\n')
f.write('222222222\n')
f.write('333\n4444\n555\n')
# f.writable()
f.writelines(['555\n','6666\n'])
f.writelines(['555\n','6666\n',1]) # 文件内容只能是字符串，只能写字符串
f.close()