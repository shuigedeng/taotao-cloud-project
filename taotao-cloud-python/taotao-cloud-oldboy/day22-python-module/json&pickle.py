# dic='{"name":"alex"}'
# f=open("hello","w")
# f.write(dic)

# f_read=open("hello","r")
# data=f_read.read()
# print(type(data))
# data=eval(data)
# print(data["name"])

# import json
#
#
# dic={'name':'alex'}#---->{"name":"alex"}----->'{"name":"alex"}'
# i=8                 #---->'8'
# s='hello'          #---->"hello"------>'"hello"'
# l=[11,22]           #---->"[11,22]"
#
# f=open("new_hello","w")

# dic_str=json.dumps(dic)
# f.write(dic_str)    #json.dump(dic,f)



# f_read=open("new_hello","r")
# data=json.loads(f_read.read())      # data=json.load(f)

#
# print(data["name"])
# print(data)
# print(type(data))

# print(s)
# print(type(s))


# data=json.dumps(dic)
#
# print(data)     #{"name": "alex"}
# print(type(data))


#注意：
# import json
#
# with open("Json_test","r") as f:
#     data=f.read()
#     data=json.loads(data)
#     print(data["name"])

#----------------------pickle-------
import pickle

dic = {'name': 'alvin', 'age': 23, 'sex': 'male'}

print(type(dic))  # <class 'dict'>

# j = pickle.dumps(dic)
# print(type(j))  # <class 'bytes'>
#
# f = open('序列化对象_pickle', 'wb')  # 注意是w是写入str,wb是写入bytes,j是'bytes'
# f.write(j)  # -------------------等价于pickle.dump(dic,f)
#
# f.close()
# # -------------------------反序列化
import pickle

f = open('序列化对象_pickle', 'rb')

data = pickle.loads(f.read())  # 等价于data=pickle.load(f)

print(data['age'])

# # -------------------------shelve模块---------
import shelve

f = shelve.open(r'shelve1')  # 目的：将一个字典放入文本 f={}
#
# f['stu1_info']={'name':'alex','age':'18'}
# f['stu2_info']={'name':'alvin','age':'20'}
# f['school_info']={'website':'oldboyedu.com','city':'beijing'}
# f.close()

print(f.get('stu1_info')['age'])



# dic={}
#
# dic["name"]="alvin"
# dic["info"]={"name":"alex"}

