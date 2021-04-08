from django.db import models

class Classes(models.Model):
    """
    班级表,男
    """
    titile = models.CharField(max_length=32)
    m = models.ManyToManyField('Teachers',related_name='sssss')

class Teachers(models.Model):
    """
    老师表，女
    """
    name = models.CharField (max_length=32)

class Student(models.Model):
    """
    学生表
    """
    username = models.CharField(max_length=32)
    age = models.IntegerField()
    gender = models.BooleanField()
    cs = models.ForeignKey(Classes,related_name='ssss') # cs,cs_id  1    3班



######################## 单表 ########################
# 增加
# Teachers.objects.create(name='root')
# obj = Teachers(name='root')
# obj.save()
# 查
# Teachers.objects.all()
# Teachers.objects.filter(id=1)
# Teachers.objects.filter(id=1,name='root')
# result = Teachers.objects.filter(id__gt=1)
# [obj(id,name),]
# result = Teachers.objects.filter(id__gt=1).first()
# 删除
# Teachers.objects.filter(id=1).delete()
# 改
# Teachers.objects.all().update(name='alex')
# Teachers.objects.filter(id=1).update(name='alex')

######################## 一对多 ########################
"""
班级：
id    name
 1    3班
 2    6班

学生
id   username    age    gender   cs_id
1      东北       18     男         1
2      东北1      118     男        2
2      东北1      118     男        1
"""
# 增加
# Student.objects.create(username='东北',age=18,gender='男',cs_id=1)
# Student.objects.create(username='东北',age=18,gender='男',cs= Classes.objects.filter(id=1).first() )
# 查看
"""
ret = Student.objects.all()
# []
# [ obj(..),]
# [ obj(1      东北       18     男         1),obj(2      东北1      118     男         2),obj(..),]
for item in ret:
    print(item.id)
    print(item.username)
    print(item.age)
    print(item.gender)
    print(item.cs_id)
    print(item.cs.id)
    print(item.cs.name)
"""
# 删除
# Student.objects.filter(id=1).delete()
# Student.objects.filter(cs_id=1).delete()

# cid = input('请输入班级ID')
# Student.objects.filter(cs_id=cid).delete()

# cname = input('请输入班级名称')
# Student.objects.filter(cs_id=cid).delete()
# Student.objects.filter(cs__name=cname).delete()




######################## 多对多 ########################

# 多对多
"""
班级：
id  title
1    3班
2    4班
3    5班
老师：
id   title
 1    Alex
 2    老妖
 3    瞎驴
 4    Eric
 老师班级关系表(类):
id   班级id   老师id
 1     1        2
 2     1        3
 4     2        2
 5     2        3
 6     2        4
 7     1        5


# 增
obj = Classes.objects.filter(id=1).first() #1    3班
obj.m.add(2)
obj.m.add([4,3])

# obj = Classes.objects.filter(id=2).first() #1    3班
# obj.m.add(2)
# obj.m.add([4,3])

obj = Classes.objects.filter(id=1).first() #1    3班
# 删除
# obj.m.remove([4,3])
# 清空
obj.m.clear()
# 重置
obj.m.set([2,3,5])

# 查第三张表
# 把3班的所有老师列举
obj = Classes.objects.filter(id=1).frist()
obj.id
obj.titile
ret = obj.m.all() # 第三张表
# ret是一个 [ 老师1(id,name),obj(id,name)   ]

"""









