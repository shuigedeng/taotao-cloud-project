# import time
# def test():
# 	print('开始生孩子啦。。。。。。')
# 	print('开始生孩子啦。。。。。。')
# 	print('开始生孩子啦。。。。。。')
# 	yield '我' #return
# 	time.sleep(3)
# 	print('开始生儿子啦')
# 	yield '儿子'
#
# 	time.sleep(3)
# 	print('开始生孙子啦')
# 	yield '孙子'
#
#
# res=test()
# print(res)
# print(res.__next__()) #test()
# print(res.__next__()) #test()
# print(res.__next__()) #test()

# def product_baozi():
# 	ret=[]
# 	for i in range(100):
# 		ret.append('一屉包子%s' %i)
# 	return ret
# baozi_list=product_baozi()
# print(baozi_list)


# def product_baozi():
# 	for i in range(100):
# 		print('正在生产包子')
# 		yield '一屉包子%s' %i #i=1
# 		print('开始卖包子')
# pro_g=product_baozi()
#
# baozi1=pro_g.__next__()
# #加代码
# baozi1=pro_g.__next__()


# def xiadan():
# 	ret=[]
# 	for i in range(10000):
# 		ret.append('鸡蛋%s' %i)
# 	return ret
#
# print(xiadan())
#缺点1：占空间大
#缺点2：效率低

def xiadan():
	for i in range(5):
		yield '鸡蛋%s' %i

alex_lmj=xiadan()
print(alex_lmj.__next__())
print(alex_lmj.__next__())
print(alex_lmj.__next__())
print(alex_lmj.__next__())
print(alex_lmj.__next__())
print(alex_lmj.__next__())


# for jidan in alex_lmj:
# 	print(jidan)


# jidan=alex_lmj.__next__()
# jidan=alex_lmj.__next__()
# jidan=alex_lmj.__next__()
# jidan=alex_lmj.__next__()
# jidan=alex_lmj.__next__()
# jidan=alex_lmj.__next__()



# print('zhoushaochen 取鸡蛋',jidan)