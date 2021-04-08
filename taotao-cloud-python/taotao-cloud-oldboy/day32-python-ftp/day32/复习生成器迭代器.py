#生成器和迭代器
gen = (v for v in 'Python')
for v in iter(gen.__next__, 'h'):
    print(v)

