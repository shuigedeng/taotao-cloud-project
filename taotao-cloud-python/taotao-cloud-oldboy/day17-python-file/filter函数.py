# movie_people=['sb_alex','sb_wupeiqi','linhaifeng','sb_yuanhao']




# def filter_test(array):
#     ret=[]
#     for p in array:
#         if not p.startswith('sb'):
#                ret.append(p)
#     return ret
#
# res=filter_test(movie_people)
# print(res)


# movie_people=['alex_sb','wupeiqi_sb','linhaifeng','yuanhao_sb']
# def sb_show(n):
#     return n.endswith('sb')
#
# def filter_test(func,array):
#     ret=[]
#     for p in array:
#         if not func(p):
#                ret.append(p)
#     return ret
#
# res=filter_test(sb_show,movie_people)
# print(res)

#终极版本
movie_people=['alex_sb','wupeiqi_sb','linhaifeng','yuanhao_sb']
# def sb_show(n):
#     return n.endswith('sb')
#--->lambda n:n.endswith('sb')

def filter_test(func,array):
    ret=[]
    for p in array:
        if not func(p):
               ret.append(p)
    return ret

res=filter_test(lambda n:n.endswith('sb'),movie_people)
print(res)

#filter函数
movie_people=['alex_sb','wupeiqi_sb','linhaifeng','yuanhao_sb']
print(filter(lambda n:not n.endswith('sb'),movie_people))



res=filter(lambda n:not n.endswith('sb'),movie_people)
print(list(res))


print(list(filter(lambda n:not n.endswith('sb'),movie_people)))