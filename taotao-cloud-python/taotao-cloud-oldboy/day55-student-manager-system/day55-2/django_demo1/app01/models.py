from django.db import models

class Classes(models.Model):
    """
    班级表,男
    """
    titile = models.CharField(max_length=32)
    m = models.ManyToManyField("Teachers")

class Teachers(models.Model):
    """
    老师表，女
    """
    name = models.CharField (max_length=32)

"""
cid_id  tid_id
 1    1
 1    2
 6    1
 1000  1000
"""
# class C2T(models.Model):
#     cid = models.ForeignKey(Classes)
#     tid = models.ForeignKey(Teachers)

class Student(models.Model):
    username = models.CharField(max_length=32)
    age = models.IntegerField()
    gender = models.BooleanField()
    cs = models.ForeignKey(Classes)



