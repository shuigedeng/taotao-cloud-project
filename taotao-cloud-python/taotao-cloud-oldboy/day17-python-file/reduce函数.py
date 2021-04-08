# from functools import reduce


# num_l=[1,2,3,100]

# res=0
# for num in num_l:
#     res+=num
#
# print(res)

# num_l=[1,2,3,100]
# def reduce_test(array):
#     res=0
#     for num in array:
#         res+=num
#     return res
#
# print(reduce_test(num_l))


# num_l=[1,2,3,100]

# def multi(x,y):
#     return x*y
#lambda x,y:x*y

# def reduce_test(func,array):
#     res=array.pop(0)
#     for num in array:
#         res=func(res,num)
#     return res
#
# print(reduce_test(lambda x,y:x*y,num_l))

num_l=[1,2,3,100]
def reduce_test(func,array,init=None):
    if init is None:
        res=array.pop(0)
    else:
        res=init
    for num in array:
        res=func(res,num)
    return res

print(reduce_test(lambda x,y:x*y,num_l,100))


#reduce函数
from functools import reduce
num_l=[1,2,3,100]
print(reduce(lambda x,y:x+y,num_l,1))
print(reduce(lambda x,y:x+y,num_l))

