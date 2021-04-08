import os

# print(os.getcwd())
# os.chdir("..")
# print(os.getcwd())

#os.makedirs('dirname1/dirname2')
#os.removedirs("dirname1/dirname2")
#print(os.listdir())
#print(os.stat("sss.py"))

# print(os.system("dir"))

print(os.path.split(r"C:\Users\Administrator\脱产三期\day22\sss.py"))
print(os.path.dirname(r"C:\Users\Administrator\脱产三期\day22\sss.py"))
print(os.path.basename(r"C:\Users\Administrator\脱产三期\day22\sss.py"))
a="C:\Users\Administrator"
b="脱产三期\day22\sss.py"

os.path.join(a,b)#  路径拼接

