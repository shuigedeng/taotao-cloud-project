from django.db import models


class UserInfo(models.Model):
    id = models.BigIntegerField(primary_key=True)
    username = models.CharField(max_length=16)
    password = models.CharField(max_length=32)
    courseId = models.BigIntegerField()

    class Meta:
        verbose_name = "课程"
        verbose_name_plural = verbose_name
