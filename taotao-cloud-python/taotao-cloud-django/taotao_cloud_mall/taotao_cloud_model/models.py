import datetime

from django.db import models


# Create your models here.


class Account(models.Model):
    id = models.BigIntegerField(primary_key=True)
    name = models.CharField(max_length=16, null=False, help_text='收款人姓名')
    phone = models.CharField(max_length=32, null=False, help_text='手机号码')
    account = models.CharField(max_length=32, null=False, help_text='账户')
    card = models.CharField(max_length=32, null=False, help_text='STRING')
    openId = models.CharField(max_length=256, null=True, help_text='微信id')
    userId = models.BigIntegerField(null=False)
    createdAt = models.DateTimeField(null=False,
                                     default=datetime.datetime.now(),
                                     help_text='创建时间')
    updateAt = models.DateTimeField(null=True, help_text='修改时间')

    class Meta:
        verbose_name = "账户"
        verbose_name_plural = verbose_name


class Address(models.Model):
    id = models.BigIntegerField(primary_key=True)
    receiverName = models.CharField(max_length=16, null=False,
                                    help_text='收货人名称')
    receiverPhone = models.CharField(max_length=32, null=False,
                                     help_text='收货手机号')
    receiverAddress = models.CharField(max_length=32, null=False,
                                       help_text='收货地址')
    orderId = models.BigIntegerField(null=False)
    createdAt = models.DateTimeField(null=False,
                                     default=datetime.datetime.now(),
                                     help_text='创建时间')
    updateAt = models.DateTimeField(null=True, help_text='修改时间')

    class Meta:
        verbose_name = "地址"
        verbose_name_plural = verbose_name


class Balance(models.Model):
    id = models.BigIntegerField(primary_key=True)
    balance = models.IntegerField(null=False, help_text='余额')
    price = models.IntegerField(null=False, help_text='金额')
    isAdd = models.BooleanField(null=False, default=False, help_text='增减与否')
    remark = models.CharField(max_length=256, null=False, help_text='备注')
    userId = models.BigIntegerField(null=False)
    storeId = models.BigIntegerField(null=False)
    orderId = models.BigIntegerField(null=False)
    topUpOrderId = models.BigIntegerField(null=False)
    createdAt = models.DateTimeField(null=False,
                                     default=datetime.datetime.now(),
                                     help_text='创建时间')
    updateAt = models.DateTimeField(null=True, help_text='修改时间')

    class Meta:
        verbose_name = "金额变动记录"
        verbose_name_plural = verbose_name


class Banner(models.Model):
    id = models.BigIntegerField(primary_key=True)
    position = models.CharField(max_length=256, null=False, help_text='位置')
    title = models.CharField(max_length=256, null=False, help_text='标题')
    imageKey = models.CharField(max_length=2560, null=True, help_text='图片')
    target = models.CharField(max_length=2560, null=True, help_text='目标')
    orderValue = models.IntegerField(null=False, default=0, help_text='排序')
    createdAt = models.DateTimeField(null=False,
                                     default=datetime.datetime.now(),
                                     help_text='创建时间')
    updateAt = models.DateTimeField(null=True, help_text='修改时间')

    class Meta:
        verbose_name = "横幅"
        verbose_name_plural = verbose_name


class Card(models.Model):
    id = models.CharField(max_length=6, primary_key=True, null=False)
    expiredDate = models.CharField(max_length=8, null=True, help_text='过期时间')
    batch = models.IntegerField(null=True, default=0, help_text='批次')
    STATUS_CHOICES = (
        ('unused'),
        ('used'),
    )
    status = models.CharField(choices=STATUS_CHOICES, default='used',
                              help_text='状态')
    orderValue = models.IntegerField(null=False, default=0, help_text='排序')
    userId = models.BigIntegerField(null=False)
    createdAt = models.DateTimeField(null=False,
                                     default=datetime.datetime.now(),
                                     help_text='创建时间')
    updateAt = models.DateTimeField(null=True, help_text='修改时间')

    class Meta:
        verbose_name = "卡片"
        verbose_name_plural = verbose_name


class Config(models.Model):
    id = models.CharField(max_length=6, primary_key=True, null=False)
    key = models.CharField(max_length=256, null=True, help_text='key')
    name = models.CharField(max_length=256, null=True, help_text='名称')
    TYPE_CHOICES = (
        ('decimal5with5'),
        ('html'),
        ('integer'),
        ('integer'),
        ('boolean'),
        ('string'),
    )
    type = models.CharField(choices=TYPE_CHOICES, default='string',
                            help_text='状态')
    str = models.CharField(max_length=256, null=True, help_text='string')
    boo = models.BooleanField(null=True, default=False, help_text='boolean')
    integer = models.IntegerField(help_text='Integer')
    decimal5with5 = models.DecimalField(decimal_places=5, default=0.0,
                                        help_text='decimal5with5')
    orderValue = models.IntegerField(null=False, default=0, help_text='排序')
    userId = models.BigIntegerField(null=False)

    createdAt = models.DateTimeField(null=False,
                                     default=datetime.datetime.now(),
                                     help_text='创建时间')
    updateAt = models.DateTimeField(null=True, help_text='修改时间')

    class Meta:
        verbose_name = "配置"
        verbose_name_plural = verbose_name


class Coupon(models.Model):
    id = models.BigIntegerField(primary_key=True)
    amount = models.IntegerField(null=False, default=0, help_text='优惠券面额')
    require = models.IntegerField(null=False, default=0, help_text=' 要求（满减）')
    usedAt = models.DateTimeField(null=True, help_text='使用时间')
    expiredDate = models.CharField(max_length=8, null=False, help_text='过期日期')
    TYPE_CHOICES = (
        ('ordinary'),
        ('special'),
    )
    type = models.CharField(choices=TYPE_CHOICES, default='ordinary',
                            help_text='类型 普通  会员')
    userId = models.BigIntegerField(null=False)
    issueCouponId = models.BigIntegerField(null=False, help_text='优惠券发放')
    orderId = models.BigIntegerField(null=False)
    createdAt = models.DateTimeField(null=False,
                                     default=datetime.datetime.now(),
                                     help_text='创建时间')
    updateAt = models.DateTimeField(null=True, help_text='修改时间')

    class Meta:
        verbose_name = "优惠券"
        verbose_name_plural = verbose_name

class DistributionUser(models.Model):
    id = models.BigIntegerField(primary_key=True)
    name = models.CharField(max_length=256, null=False, help_text='名称')
    phone = models.CharField(max_length=256, null=False, help_text='电话号码')
    orderId = models.BigIntegerField(null=False)
    createdAt = models.DateTimeField(null=False,
                                     default=datetime.datetime.now(),
                                     help_text='创建时间')
    updateAt = models.DateTimeField(null=True, help_text='修改时间')

    class Meta:
        verbose_name = "金额变动记录"
        verbose_name_plural = verbose_name

