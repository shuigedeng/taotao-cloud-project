from django.db import models


class Course(models.Model):
    id = models.BigIntegerField(primary_key=True)
    name = models.CharField(max_length=50, verbose_name="课程名")
    desc = models.CharField(max_length=300, verbose_name=u"课程描述")
    degree = models.CharField(
        choices=(("primary", '初级'), ("middle", "中级"), ("junior", "高级")),
        max_length=10, verbose_name='课程难度')
    students = models.IntegerField(default=0, verbose_name="学习人数")

    # 需要安装pillow，图片处理库
    # image = models.ImageField(upload_to="courses/%Y/%m", verbose_name="课程图片",
    #                           max_length=100)

    class Meta:
        verbose_name = "课程"
        verbose_name_plural = verbose_name
