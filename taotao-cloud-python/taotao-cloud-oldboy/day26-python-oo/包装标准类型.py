class List(list):
    def append(self, p_object):
        if type(p_object) is str:
            # self.append(p_object)
            super().append(p_object)
        else:
            print('只能添加字符串类型')

    def show_midlle(self):
        mid_index=int(len(self)/2)
        return self[mid_index]


# l2=list('hell oworld')
# print(l2,type(l2))

l1=List('helloworld')
# print(l1,type(l1))
# print(l1.show_midlle())
l1.append(1111111111111111111111)
l1.append('SB')
print(l1)