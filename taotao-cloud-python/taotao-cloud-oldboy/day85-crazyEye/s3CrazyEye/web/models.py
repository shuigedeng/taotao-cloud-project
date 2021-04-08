from django.db import models
from django.contrib.auth.models import (
    BaseUserManager, AbstractBaseUser,PermissionsMixin
)

# Create your models here.



class Host(models.Model):
    """存储主机列表"""
    name = models.CharField(max_length=64,unique=True)
    ip_addr = models.GenericIPAddressField(unique=True)
    port = models.SmallIntegerField(default=22)
    idc = models.ForeignKey("IDC")
    #remote_users = models.ManyToManyField("RemoteUser")

    def __str__(self):
        return self.name

class HostGroup(models.Model):
    """存储主机组"""
    name = models.CharField(max_length=64,unique=True)
    #hosts = models.ManyToManyField("Host")
    host_to_remote_users = models.ManyToManyField("HostToRemoteUser")

    def __str__(self):
        return self.name


class HostToRemoteUser(models.Model):
    """绑定主机和远程用户的对应关系"""
    host = models.ForeignKey("Host")
    remote_user = models.ForeignKey("RemoteUser")

    class Meta:
        unique_together = ("host","remote_user")


    def __str__(self):
        return "%s %s"%(self.host,self.remote_user)

class RemoteUser(models.Model):
    """存储远程要管理的主机的账号信息"""
    auth_type_choices = ((0,'ssh-password'),(1,'ssh-key'))
    auth_type = models.SmallIntegerField(choices=auth_type_choices,default=0)
    username = models.CharField(max_length=32)
    password = models.CharField(max_length=64,blank=True,null=True)

    class Meta:
        unique_together = ('auth_type','username','password')


    def __str__(self):
        return "%s:%s" %(self.username,self.password)

class UserProfileManager(BaseUserManager):
    def create_user(self, email, name, password=None):
        """
        Creates and saves a User with the given email, date of
        birth and password.
        """
        if not email:
            raise ValueError('Users must have an email address')

        user = self.model(
            email=self.normalize_email(email),
            name=name,
        )

        user.set_password(password)
        user.save(using=self._db)
        return user

    def create_superuser(self, email, name, password):
        """
        Creates and saves a superuser with the given email, date of
        birth and password.
        """
        user = self.create_user(
            email,
            password=password,
            name=name,
        )
        user.is_superuser = True
        user.save(using=self._db)
        return user


class UserProfile(AbstractBaseUser,PermissionsMixin):
    """堡垒机账号"""
    email = models.EmailField(
        verbose_name='email address',
        max_length=255,
        unique=True,

    )
    name = models.CharField(max_length=64, verbose_name="姓名")
    is_active = models.BooleanField(default=True)
    is_staff = models.BooleanField(default=True)
    objects = UserProfileManager()

    host_to_remote_users = models.ManyToManyField("HostToRemoteUser",blank=True,null=True)
    host_groups = models.ManyToManyField("HostGroup",blank=True,null=True)

    USERNAME_FIELD = 'email'
    REQUIRED_FIELDS = ['name']

    def get_full_name(self):
        # The user is identified by their email address
        return self.email

    def get_short_name(self):
        # The user is identified by their email address
        return self.email

    def __str__(self):              # __unicode__ on Python 2
        return self.email






class IDC(models.Model):
    """机房信息"""
    name = models.CharField(max_length=64,unique=True)

class AuditLog(models.Model):
    """存储审计日志"""