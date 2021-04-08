from django.db import models

class Img(models.Model):

    src = models.FileField(max_length=32,verbose_name='图片路径',upload_to='static/upload')
    title = models.CharField(max_length=16,verbose_name='标题')
    summary = models.CharField(max_length=128,verbose_name='简介')

    class Meta:
        verbose_name_plural = '图片'
    def __str__(self):
        return self.title







