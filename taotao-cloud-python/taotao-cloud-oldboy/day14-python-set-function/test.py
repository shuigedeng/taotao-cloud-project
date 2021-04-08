python_l=['lcg','szw','zjw']
linux_l=['lcg','szw']

python_and_linux_l=[]
for p_name in python_l:
    if p_name in linux_l:
        python_and_linux_l.append(p_name)

print(python_and_linux_l)
