# s1 = 'hello'
# s1=1
# try:
#     int(s1)
# except IndexError as e:
#     print(e)
# except KeyError as e:
#     print(e)
# except ValueError as e:
#     print(e)
# except Exception as e:
#    print(e)
# else:
#     print('try内代码块没有异常则执行我')
#
#
#
# print(1111111111111)
# print(22222222222)
# print(33333333333)




s1 = 'hello'
# s1=1
try:
    int(s1)
except IndexError as e:
    print(e)
except KeyError as e:
    print(e)
except ValueError as e:
    print(e)
except Exception as e:
   print(e)
else:
    print('try内代码块没有异常则执行我')
finally:
    print('无论异常与否,都会执行该模块')


print(1111111111111)
print(22222222222)
print(33333333333)

