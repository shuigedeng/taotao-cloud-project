# msg='i am %s my hobby is %s' % ('lhf','alex')
# print(msg)
#
# msg='i am %s my hobby is %s' % ('lhf',1)
# msg='i am %s my hobby is %s' % ('lhf',[1,2])
# print(msg)
# name='lhf'
# age=19
# msg='i am %s my hobby is %s' % (name,age)
# print(msg)

#打印浮点数
tpl = "percent %.2f" % 99.976234444444444444
print(tpl)

#打印百分比
tpl = 'percent %.2f %%' % 99.976234444444444444
print(tpl)

tpl = "i am %(name)s age %(age)d" % {"name": "alex", "age": 18}
print(tpl)

msg='i am %(name)+60s my hobby is alex' %{'name':'lhf'}
print(msg)

msg='i am \033[43;1m%(name)+60s\033[0m my hobby is alex' %{'name':'lhf'}
print(msg)


print('root','x','0','0',sep=':')
# print('root'+':'+'x'+':'+'0','0')
