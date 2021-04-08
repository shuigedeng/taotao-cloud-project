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

l=[1,3,100,-1,2]
print(max(l))
print(min(l))

