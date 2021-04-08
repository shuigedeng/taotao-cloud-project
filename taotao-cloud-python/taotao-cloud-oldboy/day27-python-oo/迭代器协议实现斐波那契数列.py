class Fib:
    def __init__(self):
        self._a=1
        self._b=1

    def __iter__(self):
        return self
    def __next__(self):
        if self._a > 100:
            raise StopIteration('终止了')
        self._a,self._b=self._b,self._a + self._b
        return self._a

f1=Fib()
print(next(f1))
print(next(f1))
print(next(f1))
print(next(f1))
print(next(f1))
print('==================================')
for i in f1:
    print(i)