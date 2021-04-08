from django.db import models

class User(models.Model):
    username = models.CharField(max_length=32)
    password = models.CharField(max_length=64)
    # m = models.ManyToManyField("Role")
    class Meta:
        verbose_name_plural = '用户表'
    def __str__(self):
        return self.username

class Role(models.Model):
    caption = models.CharField(max_length=32)
    class Meta:
        verbose_name_plural = '角色表'
    def __str__(self):
        return self.caption

class User2Role(models.Model):
    u = models.ForeignKey(User)
    r = models.ForeignKey(Role)
    class Meta:
        verbose_name_plural = '用户分配角色'
    def __str__(self):
        return "%s-%s" %(self.u.username,self.r.caption,)

class Action(models.Model):
    # get  获取用户信息1
    # post  创建用户2
    # delete 删除用户3
    # put  修改用户4
    caption = models.CharField(max_length=32)
    code = models.CharField(max_length=32)

    class Meta:
        verbose_name_plural = '操作表'
    def __str__(self):
        return self.caption

# 1    菜单1     null
# 2    菜单2     null
# 3    菜单3     null
# 4    菜单1.1    1
# 5    菜单1.2    1
# 6    菜单1.2.1  4
# 无最后一层
class Menu(models.Model):
    caption = models.CharField(max_length=32)
    parent = models.ForeignKey('self',related_name='p',null=True,blank=True)
    def __str__(self):
        return "%s" %(self.caption,)

class Permission(models.Model):
    # http://127.0.0.1:8001/user.html  用户管理 1
    # http://127.0.0.1:8001/order.html 订单管理 1
    caption = models.CharField(max_length=32)
    url = models.CharField(max_length=32)
    menu = models.ForeignKey(Menu,null=True,blank=True)
    class Meta:
        verbose_name_plural = 'URL表'
    def __str__(self):
        return "%s-%s" %(self.caption,self.url,)

class Permission2Action(models.Model):
    p = models.ForeignKey(Permission)
    a = models.ForeignKey(Action)

    class Meta:
        verbose_name_plural = '权限表'
    def __str__(self):
        return "%s-%s:-%s?t=%s" %(self.p.caption,self.a.caption,self.p.url,self.a.code,)

class Permission2Action2Role(models.Model):
    p2a = models.ForeignKey(Permission2Action)
    r = models.ForeignKey(Role)
    class Meta:
        verbose_name_plural = '角色分配权限'
    def __str__(self):
        return "%s==>%s" %(self.r.caption,self.p2a,)

