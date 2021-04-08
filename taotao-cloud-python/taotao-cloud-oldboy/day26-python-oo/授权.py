import time
class FileHandle:
    def __init__(self,filename,mode='r',encoding='utf-8'):
        # self.filename=filename
        self.file=open(filename,mode,encoding=encoding)
        self.mode=mode
        self.encoding=encoding
    def write(self,line):
        print('------------>',line)
        t=time.strftime('%Y-%m-%d %X')
        self.file.write('%s %s' %(t,line))

    def __getattr__(self, item):
        # print(item,type(item))
        # self.file.read
        return getattr(self.file,item)

f1=FileHandle('a.txt','w+')
# print(f1.file)
# print(f1.__dict__)
# print('==>',f1.read) #触发__getattr__
# print(f1.write)
f1.write('1111111111111111\n')
f1.write('cpu负载过高\n')
f1.write('内存剩余不足\n')
f1.write('硬盘剩余不足\n')
# f1.seek(0)
# print('--->',f1.read())