f=open('d.txt','rb')

for i in f:
    offs=-3
    n=0
    while True:
        f.seek(offs,2)
        data=f.readlines()
        if len(data) > 1:
            print('最后一行',data[-1])
            break
        offs*=2
