# x='{0}{0}{0}'.format('dog')
#
# print(x)

# class Date:
#     def __init__(self,year,mon,day):
#         self.year=year
#         self.mon=mon
#         self.day=day
# d1=Date(2016,12,26)
#
# x='{0.year}{0.mon}{0.day}'.format(d1)
# y='{0.year}:{0.mon}:{0.day}'.format(d1)
# z='{0.mon}-{0.day}-{0.year}'.format(d1)
# print(x)
# print(y)
# print(z)

# x='{0.year}{0.mon}{0.day}'.format(d1)
# y='{0.year}:{0.mon}:{0.day}'
# z='{0.mon}-{0.day}-{0.year}'

format_dic={
    'ymd':'{0.year}{0.mon}{0.day}',
    'm-d-y':'{0.mon}-{0.day}-{0.year}',
    'y:m:d':'{0.year}:{0.mon}:{0.day}'
}
class Date:
    def __init__(self,year,mon,day):
        self.year=year
        self.mon=mon
        self.day=day
    def __format__(self, format_spec):
        print('我执行啦')
        print('--->',format_spec)
        if not format_spec or format_spec not in format_dic:
            format_spec='ymd'
        fm=format_dic[format_spec]
        return fm.format(self)
d1=Date(2016,12,26)
# format(d1) #d1.__format__()
# print(format(d1))
print(format(d1,'ymd'))
print(format(d1,'y:m:d'))
print(format(d1,'m-d-y'))
print(format(d1,'m-d:y'))
print('===========>',format(d1,'asdfasdfsadfasdfasdfasdfasdfasdfasdfasdfasdfasdfasd'))
