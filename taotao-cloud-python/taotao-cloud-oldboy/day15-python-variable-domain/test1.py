def calc(n):
    print(n)
    if int(n / 2) == 0: # 10/2  5/2  2/2
        return n
    res=calc(int(n / 2))
    return res


calc(10)