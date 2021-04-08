


def get_polulation():
    with open('人口普查', 'r', encoding='utf-8') as f:
        for i in f:
            yield i


g=get_polulation()
g.__next__()
g.__next__()
g.__next__()
g.__next__()
# print(g.__next__()['population'])
# s1=eval(g.__next__())
# print(type(s1))
# print(s1['population'])
# res=0
# for p in g:
#     p_dic=eval(p)
#     print(p_dic['population'])
#     res+=p_dic['population']
# print(res)

all_pop=sum(eval(i)['population'] for i in g)
print(all_pop)


for p in g:
    print('%s %%' %eval(p)['population']/all_pop)