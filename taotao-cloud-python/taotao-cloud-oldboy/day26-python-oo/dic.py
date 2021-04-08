# v = dict.fromkeys(["k1","123","323"],123)
# print(v)
# dic = {'k1': 123, 456: 123, '999': 123}
# v = dic.pop()
# print(v,dic)
# del dic["999"]
# print(dic)
# dic = {'k1': 123, 123: 123, '999': 123}
# v = dic.setdefault("k2","123")
# print(v,dic)

# dic.update({"k3":"v3"})
# dic.update(k4="v4")
# for i in dic.keys():
#     print(i)
# for i in dic.values():
#     print(i)
# for i in dic:
#     print(i,dic[i])
# for i,v in dic.items():
#     print(i,":",v)
dic = {'k1':123,123:123,'999':123}
for i,v in enumerate(dic.items(),1):
    print(i,v[0],v[1])
for i,v in enumerate(dic,1):
    print(i,v,dic[v])
