import sys,os
#sys
# li = sys.argv
#
# if li[1] == "post":
#     print("post")
# elif li[1] == "down":
#     print("1111")
'''
sys.argv   #在命令行参数是一个空列表，在其他中第一个列表元素中程序本身的路径
sys.exit(n) #退出程序，正常退出时exit(0)
sys.version  #获取python解释程序的版本信息
sys.path #返回模块的搜索路径，初始化时使用python PATH环境变量的值
sys.platform #返回操作系统平台的名称
sys.stdin    #输入相关
sys.stdout  #输出相关
sys.stderror #错误相关

# 常用sys模块的方法
'''
# print(sys.path)
# print(os.getcwd())
# os.chdir("E:\\linuxvideo")
# print(os.getcwd())
# makedirs(name, mode=0o777, exist_ok=False):
# os.makedirs(r"aa\bb\cc")
# os.removedirs(r"aa\bb")
# print(os.listdir(os.getcwd()))
# print(os.stat("file.py").st_size)

# os.rename("aa","bb")
#
# print(__file__) #获取当前文件路劲的文件名
# print(os.path.abspath(__file__))
# print(os.path.split(os.path.abspath(__file__)))
print(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))  #返回当前文件的上一级目录
# # os.path.join(path1[, path2[, ...]])  将多个路径组合后返回，第一个绝对路径之前的参数将被忽略
# print(os.path.join("d:\\"))
# print(os.path.join("d:\\","www","baidu","a.py"))
print(os.getcwd())
print(os.path.dirname(os.path.abspath(__file__)))
print(os.path.join(os.path.dirname(os.path.abspath(__file__)),"bb","123.txt"))






