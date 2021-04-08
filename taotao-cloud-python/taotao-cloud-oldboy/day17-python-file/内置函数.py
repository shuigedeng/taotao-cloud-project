# print(abs(-1))
# print(abs(1))
#
# print(all([1,2,'1']))
# print(all([1,2,'1','']))
# print(all(''))

# print(any([0,'']))
# print(any([0,'',1]))


# print(bin(3))

#空，None，0的布尔值为False，其余都为True
# print(bool(''))
# print(bool(None))
# print(bool(0))

# name='你好'
# print(bytes(name,encoding='utf-8'))
# print(bytes(name,encoding='utf-8').decode('utf-8'))
#
# print(bytes(name,encoding='gbk'))
# print(bytes(name,encoding='gbk').decode('gbk'))
#
# print(bytes(name,encoding='ascii'))#ascii不能编码中文

# print(chr(46))

# print(dir(dict))

# print(divmod(10,3))

# dic={'name':'alex'}
# dic_str=str(dic)
# print(dic_str)

#可hash的数据类型即不可变数据类型，不可hash的数据类型即可变数据类型
# print(hash('12sdfdsaf3123123sdfasdfasdfasdfasdfasdfasdfasdfasfasfdasdf'))
# print(hash('12sdfdsaf31231asdfasdfsadfsadfasdfasdf23'))
#
# name='alex'
# print(hash(name))
# print(hash(name))
#
#
# print('--->before',hash(name))
# name='sb'
# print('=-=>after',hash(name))


# print(help(all))

# print(bin(10))#10进制->2进制
# print(hex(12))#10进制->16进制
# print(oct(12))#10进制->8进制



# print(isinstance(1,int))
# print(isinstance('abc',str))
# print(isinstance([],list))
# print(isinstance({},dict))
# print(isinstance({1,2},set))

name='哈哈哈哈哈哈哈哈哈哈哈哈哈哈啊哈粥少陈'
# print(globals())
# print(__file__)
#
# def test():
#     age='1111111111111111111111111111111111111111111111111111111111111'
#     # print(globals())
#     print(locals())
#
# test()

# l=[1,3,100,-1,2]
# print(max(l))
# print(min(l))
#
#
# print(list(zip(('a','n','c'),(1,2,3))))
# print(list(zip(('a','n','c'),(1,2,3,4))))
# print(list(zip(('a','n','c','d'),(1,2,3))))
#
# p={'name':'alex','age':18,'gender':'none'}
# print(list(zip(p.keys(),p.values())))
# # print(list(p.keys()))
# # print(list(p.values()))
#
# print(list(zip(['a','b'],'12345')))


# l=[1,3,100,-1,2]
# print(max(l))
# print(min(l))


age_dic={'alex_age':18,'wupei_age':20,'zsc_age':100,'lhf_age':30}

# print(max(age_dic.values()))
#
# #默认比较的是字典的key
# # print(max(age_dic))
#
# for item in zip(age_dic.values(),age_dic.keys()): #[(18,'alex_age')  (20,'wupeiqi_age') () () ()]
#     print(item)
#
# print('=======>',list(max(zip(age_dic.values(),age_dic.keys()))))

# l=[
#     (5,'e'),
#     (1,'b'),
#     (3,'a'),
#     (4,'d'),
# ]
# # l1=['a10','b12','c10',100] #不同类型之间不能进行比较
# l1=['a10','a2','a10'] #不同类型之间不能进行比较
# print(list(max(l)))
# print('--->',list(max(l1)))



# l=[1,3,100,-1,2]
# print(max(l))
# dic={'age1':18,'age2':10}
# print(max(dic)) #比较的是key
# print(max(dic.values())) #比较的是key，但是不知道是那个key对应的
#
# print(max(zip(dic.values(),dic.keys()))) #结合zip使用
#
#
# people=[
#     {'name':'alex','age':1000},
#     {'name':'wupei','age':10000},
#     {'name':'yuanhao','age':9000},
#     {'name':'linhaifeng','age':18},
# ]
# # max(people,key=lambda dic:dic['age'])
# print('周绍陈取出来没有',max(people,key=lambda dic:dic['age']))

# ret=[]
# for item in people:
#     ret.append(item['age'])
# print(ret)
# max(ret)


# print(chr(97))
# print(ord('a'))

# print(pow(3,3))  #3**3
# print(pow(3,3,2))  #3**3%2



# l=[1,2,3,4]
# print(list(reversed(l)))
# print(l)

#
# print(round(3.5))

# print(set('hello'))

# l='hello'
# s1=slice(3,5)
# s2=slice(1,4,2)
# # print(l[3:5])
# print(l[s1])
# print(l[s2])
# print(s2.start)
# print(s2.stop)
# print(s2.step)



# l=[3,2,1,5,7]
# l1=[3,2,'a',1,5,7]
# print(sorted(l))
# # print(sorted(l1)) #排序本质就是在比较大小，不同类型之间不可以比较大小
# people=[
#     {'name':'alex','age':1000},
#     {'name':'wupei','age':10000},
#     {'name':'yuanhao','age':9000},
#     {'name':'linhaifeng','age':18},
# ]
# print(sorted(people,key=lambda dic:dic['age']))
# name_dic={
#     'abyuanhao': 11900,
#     'alex':1200,
#     'wupei':300,
# }
# print(sorted(name_dic))
#
# print(sorted(name_dic,key=lambda key:name_dic[key]))
#
# print(sorted(zip(name_dic.values(),name_dic.keys())))

# print(str('1'))
# print(type(str({'a':1})))
# dic_str=str({'a':1})
# print(type(eval(dic_str)))

# l=[1,2,3,4]
# print(sum(l))
# print(sum(range(5)))
#
#
# print(type(1))
#
# msg='123'
#
# if type(msg) is str:
#     msg=int(msg)
#     res=msg+1
#     print(res)
#
# def test():
#     msg='撒旦法阿萨德防撒旦浪费艾丝凡阿斯蒂芬'
#     print(locals())
#     print(vars())
# test()
# print(vars(int))

#import------>sys----->__import__()
# import test
# test.say_hi()

# import 'test'#报错
module_name='test'
m=__import__(module_name)
m.say_hi()