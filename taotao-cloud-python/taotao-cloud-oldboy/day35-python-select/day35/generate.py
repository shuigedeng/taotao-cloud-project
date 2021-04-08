

def f():
    print("ok")
    s=yield 6
    print(s)
    print("ok2")
    yield


gen=f()
# print(gen)

# next(gen)
RET=gen.__next__()
print(RET)

# next(gen)
gen.send(5)


