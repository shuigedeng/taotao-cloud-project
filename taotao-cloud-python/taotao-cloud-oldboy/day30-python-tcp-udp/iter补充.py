# l=['a','b','c','d']
#
# def test():
#     return l.pop()
#
# x=iter(test,'b')
# print(x.__next__())
# print(x.__next__())
# print(x.__next__())


from functools import partial
def add(x,y):
    return x+y
# print(add(1,2))

func=partial(add,1) #偏函数
print(func(1))
print(func(2))

# recv_size = 0
# recv_msg = b''
# while recv_size < length:
#     recv_msg += tcp_client.recv(buffer_size)
#     recv_size = len(recv_msg)  #1024

# ''.join(iter(partial(tcp_client.recv,1024),b''))

