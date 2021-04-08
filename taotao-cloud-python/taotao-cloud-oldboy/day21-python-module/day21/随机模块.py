
import random

# ret=random.random()
# ret=random.randint(1,3)
# ret=random.randrange(1,3)
# ret=random.choice([11,22,33,44,55])
# ret=random.sample([11,22,33,44,55],2)
# ret=random.uniform(1,4)
#
# print(ret)
# ret=[1,2,3,4,5]
# random.shuffle(ret)
# print(ret)

def v_code():
    ret=""
    for i in range(5):
        num=random.randint(0,9)
        alf=chr(random.randint(65,122))
        s=str(random.choice([num,alf]))
        ret+=s
    return ret
print(v_code())