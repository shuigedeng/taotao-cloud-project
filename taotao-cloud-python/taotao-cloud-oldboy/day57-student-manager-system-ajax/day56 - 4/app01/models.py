from django.db import models
from django.db import models

class Classes(models.Model):
    """
    班级表,男
    """
    title = models.CharField(max_length=32)
    m = models.ManyToManyField("Teachers")

class Teachers(models.Model):
    """
    老师表，女
    """
    name = models.CharField (max_length=32)

class Student(models.Model):
    username = models.CharField(max_length=32)
    age = models.IntegerField()
    gender = models.BooleanField()
    cs = models.ForeignKey(Classes)
