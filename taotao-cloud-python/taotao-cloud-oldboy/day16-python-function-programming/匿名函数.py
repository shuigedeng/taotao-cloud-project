# lambda x:x+1


def calc(x):
    return x+1

res=calc(10)
print(res)
print(calc)

print(lambda x:x+1)
func=lambda x:x+1
print(func(10))




name='alex' #name='alex_sb'
def change_name(x):
    return name+'_sb'

res=change_name(name)
print(res)

func=lambda x:x+'_sb'
res=func(name)
print('匿名函数的运行结果',res)


# func=lambda x,y,z:x+y+z
# print(func(1,2,3))

name1='alex'
name2='sbalex'
name1='supersbalex'



# def test(x,y,z):
#     return x+1,y+1  #----->(x+1,y+1)

# lambda x,y,z:(x+1,y+1,z+1)