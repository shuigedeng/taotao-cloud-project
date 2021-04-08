from django.db import models

# class UserInfo(models.Model):
#
#     username = models.CharField(
#         null=True,
#         db_column='user',
#         max_length=32,
#         db_index=True,  # 只能加速查找
#         verbose_name='用户名',
#         help_text='...'
#     )
#     user_type_choices = [
#         (0,'普通用户'),
#         (1,'超级用户'),
#         (2,'VIP'),
#     ]
#     user_type = models.IntegerField(
#         choices= user_type_choices
#     )
#
#
# class SomeBody(models.Model):
#     caption = models.CharField(max_length=12)
#     pk = models.ForeignKey(
#         to="UserInfo",
#         to_field='id',
#         # related_name='b'
#         related_query_name='b'
#     )

# class User(models.Model):
#     username = models.CharField(max_length=32,db_index=True)
#
#     d = models.ManyToManyField('User',related_name='b')
#     def __str__(self):
#         return self.username

# class Date(models.Model):
#     u1 = models.IntegerField()
#     u2 = models.IntegerField()
# 1  2
# 1  1
# 1  3
# 1  4
# 2  4
# class Tag(models.Model):
#     title = models.CharField(max_length=16)
#     def __str__(self):
#         return self.title
    # m = models.ManyToManyField(
    #     to='User',
    #     through='UserToTag',
    #     through_fields=['u','t']
    # )
    # 使用ManyToManyField只能在第三张表中创建三列数据

# class UserToTag(models.Model):
#     # nid = models.AutoField(primary_key=True)
#     u = models.ForeignKey(to='User')
#     t = models.ForeignKey(to='Tag')
#     ctime = models.DateField()
#     class Meta:
#         unique_together=[
#             ('u','t'),
#         ]

class UserType(models.Model):
    title = models.CharField(max_length=32)

class Test(models.Model):
    title = models.CharField(max_length=32)
class Person(models.Model):
    user = models.CharField(max_length=32)
    ut = models.ForeignKey(UserType)
    te = models.ForeignKey(Test)











