# def calc(n):
#     print(n)
#     if int(n / 2) == 0:
#         return n
#     res=calc(int(n / 2))
#     return res
#
#
# res=calc(10)
# print(res)

# while True:
#     print(10)
#
# import time
#
# person_list=['alex','wupeiqi','linhaifeng','zsc']
# def ask_way(person_list):
#     print('-'*60)
#     if len(person_list) == 0:
#         return '根本没人知道'
#     person=person_list.pop(0)
#     if person == 'linhaifeng':
#         return '%s说:我知道,老男孩就在沙河汇德商厦,下地铁就是' %person
#
#     print('hi 美男[%s],敢问路在何方' % person)
#     print('%s回答道:我不知道,但念你慧眼识猪,你等着,我帮你问问%s...' % (person, person_list))
#     time.sleep(100)
#     res=ask_way(person_list)
#
#
#     print('%s问的结果是: %res' %(person,res))
#     return res
#
# res=ask_way(person_list)
# print(res)
# 我          来英                杨建         是征文
# res等沙河      res等沙河       res等 沙河      return ‘沙河’
#                 print          printres


import time
res=time.sleep(100)
print('----------->')