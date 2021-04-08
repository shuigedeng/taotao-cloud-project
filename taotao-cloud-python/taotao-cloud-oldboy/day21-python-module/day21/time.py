import time

#时间戳  #计算
# print(time.time())    #1481321748.481654秒

#结构化时间---当地时间
# print(time.localtime(1531242343))
# t=time.localtime()
# print(t.tm_year)
# print(t.tm_wday)
# #-----#结构化时间---UTC
# print(time.gmtime())

#-----将结构化时间转换成时间戳

# print(time.mktime(time.localtime()))
#------将结构化时间转成字符串时间strftime
#print(time.strftime("%Y---%m-%d %X",time.localtime()))
#------将字符串时间转成结构化时间strptime
#print(time.strptime("2016:12:24:17:50:36","%Y:%m:%d:%X"))

# print(time.asctime())
# print(time.ctime())


# import datetime
# print(datetime.datetime.now())


