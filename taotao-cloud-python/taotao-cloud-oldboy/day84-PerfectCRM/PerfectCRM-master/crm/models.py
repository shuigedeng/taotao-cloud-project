from django.db import models
from crm import auth
from django import forms
from django.utils.translation import ugettext_lazy as _
from django.utils.safestring import mark_safe


class Customer(models.Model):
    '''存储所有客户信息'''
    #客户在咨询时，多是通过qq,所以这里就把qq号做为唯一标记客户的值，不能重复
    qq = models.CharField(max_length=64,unique=True,help_text=u'QQ号必须唯一')
    qq_name = models.CharField(u'QQ名称',max_length=64,blank=True,null=True)
    #客户只要没报名，你没理由要求人家必须告诉你真实姓名及其它更多私人信息呀
    name = models.CharField(u'姓名',max_length=32,blank=True,null=True)
    sex_type = (('male',u'男'),('female',u'女'))
    sex = models.CharField(u"性别",choices=sex_type,default='male',max_length=32)
    birthday = models.DateField(u'出生日期',max_length=64,blank=True,null=True,help_text="格式yyyy-mm-dd")
    phone = models.BigIntegerField(u'手机号',blank=True,null=True)
    email = models.EmailField(u'常用邮箱',blank=True,null=True)
    id_num = models.CharField(u'身份证号',blank=True,null=True,max_length=64)
    source_type = (('qq',u"qq群"),
                   ('referral',u"内部转介绍"),
                   ('website',u"官方网站"),
                   ('baidu_ads',u"百度广告"),
                   ('qq_class',u"腾讯课堂"),
                   ('school_propaganda',u"高校宣讲"),
                   ('51cto',u"51cto"),
                   ('others',u"其它"),
                   )
    #这个客户来源渠道是为了以后统计各渠道的客户量＼成单量，先分类出来
    source = models.CharField(u'客户来源',max_length=64, choices=source_type,default='qq')
    #我们的很多新客户都是老学员转介绍来了，如果是转介绍的，就在这里纪录是谁介绍的他，前提这个介绍人必须是我们的老学员噢，要不然系统里找不到
    referral_from = models.ForeignKey('self',verbose_name=u"转介绍自学员",help_text=u"若此客户是转介绍自内部学员,请在此处选择内部＼学员姓名",blank=True,null=True,related_name="internal_referral", on_delete=models.CASCADE)
    #已开设的课程单独搞了张表，客户想咨询哪个课程，直接在这里关联就可以
    course = models.ForeignKey("Course",verbose_name=u"咨询课程", on_delete=models.CASCADE)
    class_type_choices = (('online', u'网络班'),
                          ('offline_weekend', u'面授班(周末)',),
                          ('offline_fulltime', u'面授班(脱产)',),
                          )
    class_type = models.CharField(u"班级类型",max_length=64,choices=class_type_choices)
    customer_note = models.TextField(u"客户咨询内容详情",help_text=u"客户咨询的大概情况,客户个人信息备注等...")
    work_status_choices = (('employed','在职'),('unemployed','无业'))
    work_status = models.CharField(u"职业状态",choices=work_status_choices,max_length=32,default='employed')
    company = models.CharField(u"目前就职公司",max_length=64,blank=True,null=True)
    salary = models.CharField(u"当前薪资",max_length=64,blank=True,null=True)
    status_choices = (('signed',u"已报名"),('unregistered',u"未报名"))
    status = models.CharField(u"状态",choices=status_choices,max_length=64,default=u"unregistered",help_text=u"选择客户此时的状态")
    #课程顾问很得要噢，每个招生老师录入自己的客户
    consultant = models.ForeignKey("UserProfile",verbose_name=u"课程顾问", on_delete=models.CASCADE)
    date = models.DateField(u"咨询日期",auto_now_add=True)

    def __str__(self):
        return u"QQ:%s -- Name:%s" %(self.qq,self.name)

    class Meta: #这个是用来在admin页面上展示的，因为默认显示的是表名，加上这个就变成中文啦
        verbose_name = u'客户信息表'
        verbose_name_plural = u"客户信息表"

    def clean_status(self):
        status = self.cleaned_data['status']
        if self.instance.id == None:  # add form
            if status == "signed":
                raise forms.ValidationError(("必须走完报名流程后，此字段才能改名已报名"))
            else:
                return status

        else:
            return status

    def clean_consultant(self):
        consultant = self.cleaned_data['consultant']

        if self.instance.id == None :#add form
            return self._request.user

        elif consultant.id != self.instance.consultant.id:
            raise forms.ValidationError(('Invalid value: %(value)s 课程顾问不允许被修改,shoud be %(old_value)s'),
                                         code='invalid',
                                         params={'value': consultant,'old_value':self.instance.consultant})
        else:
            return consultant


class Enrollment(models.Model):
    '''存储学员报名的信息'''

    #所有报名的学生 肯定是来源于客户信息表的，先咨询，后报名嘛
    customer = models.ForeignKey(Customer, on_delete=models.CASCADE)
    school = models.ForeignKey('Branch', verbose_name='校区', on_delete=models.CASCADE)

    #选择他报的班级，班级是关联课程的，比如python开发10期
    course_grade = models.ForeignKey("ClassList", verbose_name="所报班级", on_delete=models.CASCADE)
    why_us = models.TextField("为什么报名老男孩", max_length=1024, default=None, blank=True, null=True)
    your_expectation = models.TextField("学完想达到的具体期望", max_length=1024, blank=True, null=True)
    contract_agreed = models.BooleanField("我已认真阅读完培训协议并同意全部协议内容")
    contract_approved = models.BooleanField("审批通过", help_text=u"在审阅完学员的资料无误后勾选此项,合同即生效")
    enrolled_date = models.DateTimeField(auto_now_add=True, auto_created=True,
                                         verbose_name="报名日期")
    memo = models.TextField('备注', blank=True, null=True)

    def __str__(self):
        return "<%s  课程:%s>" %(self.customer ,self.course_grade)

    class Meta:
        verbose_name = '学员报名表'
        verbose_name_plural = "学员报名表"
        unique_together = ("customer", "course_grade")
        #这里为什么要做个unique_together联合唯一？因为老男孩有很多个课程， 学生学完了一个觉得好的话，以后还可以再报其它班级，
        #每报一个班级，就得单独创建一条报名记录，所以这里想避免重复数据的话，就得搞个"客户 + 班级"的联合唯一喽


class CustomerFollowUp(models.Model):
    '''存储客户的后续跟进信息'''
    customer = models.ForeignKey(Customer,verbose_name=u"所咨询客户", on_delete=models.CASCADE)
    note = models.TextField(u"跟进内容...")
    status_choices = ((1,u"近期无报名计划"),
                      (2,u"2个月内报名"),
                      (3,u"1个月内报名"),
                      (4,u"2周内报名"),
                      (5,u"1周内报名"),
                      (6,u"2天内报名"),
                      (7,u"已报名"),
                      (8,u"已交全款"),
                      )
    status = models.IntegerField(u"状态",choices=status_choices,help_text=u"选择客户此时的状态")

    consultant = models.ForeignKey("UserProfile",verbose_name=u"跟踪人", on_delete=models.CASCADE)
    date = models.DateField(u"跟进日期",auto_now_add=True)

    def __str__(self):
        return u"%s, %s" %(self.customer,self.status)

    class Meta:
        verbose_name = u'客户咨询跟进记录'
        verbose_name_plural = u"客户咨询跟进记录"

class ClassList(models.Model):
    '''存储班级信息'''
    #创建班级时需要选择这个班所学的课程
    branch = models.ForeignKey("Branch",verbose_name="校区", on_delete=models.CASCADE)
    course = models.ForeignKey("Course",verbose_name=u"课程", on_delete=models.CASCADE)
    class_type_choices = ((0,'面授'),(1,'随到随学网络'))
    class_type = models.SmallIntegerField(choices=class_type_choices,default=0)
    total_class_nums = models.PositiveIntegerField("课程总节次",default=10)
    semester = models.IntegerField(u"学期")
    price = models.IntegerField(u"学费", default=10000)
    start_date = models.DateField(u"开班日期")
    graduate_date = models.DateField(u"结业日期", blank=True, null=True)
    #选择这个班包括的讲师，可以是多个
    teachers = models.ManyToManyField("UserProfile", verbose_name=u"讲师")

    def __str__(self):
         return "%s(%s)" % (self.course, self.semester)

    class Meta:
        verbose_name = u'班级列表'
        verbose_name_plural = u"班级列表"
        #为避免重复创建班级，课程名＋学期做联合唯一
        unique_together = ("course", "semester")
    #自定义方法，反向查找每个班级学员的数量，在后台admin里 list_display加上这个"get_student_num"就可以看到
    def get_student_num(self):
        return "%s" % self.customer_set.select_related().count()

    get_student_num.short_description = u'学员数量'

class Course(models.Model):
    '''存储所开设课程的信息'''
    name = models.CharField(u"课程名",max_length=64,unique=True)
    description = models.TextField("课程描述")
    outline = models.TextField("课程大纲")
    period  = models.IntegerField("课程周期(Month)")

    def __str__(self):
        return self.name


    class Meta:
        verbose_name = '课程'
        verbose_name_plural = "课程"


class CourseRecord(models.Model):
    '''存储各班级的上课记录'''

    #讲师创建上课纪录时要选择是上哪个班的课
    course = models.ForeignKey(ClassList, verbose_name="班级(课程)", on_delete=models.CASCADE)
    day_num = models.IntegerField("节次", help_text="此处填写第几节课或第几天课程...,必须为数字")
    date = models.DateField(auto_now_add=True, verbose_name="上课日期")
    teacher = models.ForeignKey("UserProfile", verbose_name="讲师", on_delete=models.CASCADE)
    has_homework = models.BooleanField(default=True, verbose_name="本节有作业")
    homework_title = models.CharField(max_length=128,blank=True,null=True)
    homework_requirement = models.TextField(blank=True,null=True)

    def __str__(self):
        return "%s 第%s天" % (self.course, self.day_num)

    class Meta:
        verbose_name = '上课纪录'
        verbose_name_plural = "上课纪录"
        unique_together = ('course', 'day_num')


class StudyRecord(models.Model):
    '''存储所有学员的详细的学习成绩情况'''
    student = models.ForeignKey("Customer",verbose_name=u"学员", on_delete=models.CASCADE)
    course_record = models.ForeignKey(CourseRecord, verbose_name=u"第几天课程", on_delete=models.CASCADE)
    record_choices = (('checked', u"已签到"),
                      ('late',u"迟到"),
                      ('noshow',u"缺勤"),
                      ('leave_early',u"早退"),
                      )
    record = models.CharField(u"上课纪录",choices=record_choices,default="checked",max_length=64)
    score_choices = ((100, 'A+'),   (90,'A'),
                     (85,'B+'),     (80,'B'),
                     (70,'B-'),     (60,'C+'),
                     (50,'C'),      (40,'C-'),
                     (-50,'D'),       (0,'N/A'),
                     (-100,'COPY'), (-1000,'FAIL'),
                     )
    score = models.IntegerField(u"本节成绩",choices=score_choices,default=-1)
    date = models.DateTimeField(auto_now_add=True)
    note = models.CharField(u"备注",max_length=255,blank=True,null=True)

    def __str__(self):
        return u"%s,学员:%s,纪录:%s, 成绩:%s" %(self.course_record,self.student.name,self.record,self.get_score_display())

    class Meta:
        verbose_name = u'学员学习纪录'
        verbose_name_plural = u"学员学习纪录"
        #一个学员，在同一节课只可能出现一次，所以这里把course_record ＋ student 做成联合唯一
        unique_together = ('course_record','student')



class UserProfile(auth.AbstractBaseUser, auth.PermissionsMixin):
    email = models.EmailField(
        verbose_name='email address',
        max_length=255,
        unique=True,

    )
    password = models.CharField(_('password'), max_length=128,
                                help_text=mark_safe('''<a class='btn-link' href='password'>重置密码2</a>'''))

    is_active = models.BooleanField(default=True)
    is_admin = models.BooleanField(default=False)
    is_staff = models.BooleanField(
        verbose_name='staff status',
        default=True,
        help_text='Designates whether the user can log into this admin site.',
    )
    name = models.CharField(max_length=32)
    branch = models.ForeignKey("Branch",verbose_name="所属校区",blank=True,null=True, on_delete=models.CASCADE)
    roles = models.ManyToManyField('Role',blank=True)
    memo = models.TextField('备注', blank=True, null=True, default=None)
    date_joined = models.DateTimeField(blank=True, null=True, auto_now_add=True)

    USERNAME_FIELD = 'email'
    # REQUIRED_FIELDS = ['name','token','department','tel','mobile','memo']
    REQUIRED_FIELDS = ['name']

    def get_full_name(self):
        # The user is identified by their email address
        return self.email

    def get_short_name(self):
        # The user is identified by their email address
        return self.email

    def __str__(self):  # __str__ on Python 2
        return self.email

    # def has_perm(self, perm, obj=None):
    #     "Does the user have a specific permission?"
    #     # Simplest possible answer: Yes, always
    #     return True
    def has_perms(self, perm, obj=None):
        "Does the user have a specific permission?"
        # Simplest possible answer: Yes, always
        return True

    def has_module_perms(self, app_label):
        "Does the user have permissions to view the app `app_label`?"
        # Simplest possible answer: Yes, always
        return True


    @property
    def is_superuser(self):
        "Is the user a member of staff?"
        # Simplest possible answer: All admins are staff
        return self.is_admin

    class Meta:
        verbose_name = '用户信息'
        verbose_name_plural = u"用户信息"


    objects = auth.UserManager()

    class Meta:
        verbose_name = 'CRM账户'
        verbose_name_plural = 'CRM账户'

        permissions = (
            ('crm_table_list', '可以访问 kingadmin 每个表的数据列表页'),
            ('crm_table_index', '可以访问 kingadmin 首页'),
            ('crm_table_list_view', '可以访问 kingadmin 每个表中对象的修改页'),
            ('crm_table_list_change', '可以修改 kingadmin 每个表中对象'),
            ('crm_table_list_action', '可以操作 每个表的 action 功能'),
            ('crm_can_access_my_clients', '可以访问 自己的 客户列表'),

        )




class StuAccount(models.Model):
    '''存储学员账户信息'''
    account = models.OneToOneField("Customer", on_delete=models.CASCADE)
    password = models.CharField(max_length=128)
    valid_start = models.DateTimeField("账户有效期开始",blank=True,null=True)
    valid_end = models.DateTimeField("账户有效期截止",blank=True,null=True)

    def __str__(self):
        return self.account.customer.name

class Role(models.Model):
    '''角色信息'''
    name = models.CharField(max_length=32,unique=True)
    menus = models.ManyToManyField('FirstLayerMenu',blank=True)

    def __str__(self):
        return self.name


    class Meta:
        verbose_name = "角色"
        verbose_name_plural = "角色"

class Branch(models.Model):
    '''存储所有校区'''
    name = models.CharField(max_length=64,unique=True)
    def __str__(self):
        return self.name


    class Meta:
        verbose_name = "校区"
        verbose_name_plural = "校区"

class FirstLayerMenu(models.Model):
    '''第一层侧边栏菜单'''
    name = models.CharField('菜单名',max_length=64)
    url_type_choices = ((0,'related_name'),(1,'absolute_url'))
    url_type = models.SmallIntegerField(choices=url_type_choices,default=0)
    url_name = models.CharField(max_length=64,unique=True)
    order = models.SmallIntegerField(default=0,verbose_name='菜单排序')
    sub_menus = models.ManyToManyField('SubMenu',blank=True)

    def __str__(self):
        return self.name

    class Meta:
        verbose_name = "第一层菜单"
        verbose_name_plural = "第一层菜单"


class SubMenu(models.Model):
    '''第二层侧边栏菜单'''

    name = models.CharField('二层菜单名', max_length=64)
    url_type_choices = ((0,'related_name'),(1,'absolute_url'))
    url_type = models.SmallIntegerField(choices=url_type_choices,default=0)
    url_name = models.CharField(max_length=64, unique=True)
    order = models.SmallIntegerField(default=0, verbose_name='菜单排序')

    def __str__(self):
        return self.name


    class Meta:
        verbose_name = "第二层菜单"
        verbose_name_plural = "第二层菜单"


class PaymentRecord(models.Model):
    enrollment = models.ForeignKey("Enrollment", on_delete=models.CASCADE)
    pay_type_choices = (('deposit', u"订金/报名费"),
                        ('tution', u"学费"),
                        ('refund', u"退款"),
                        )
    pay_type = models.CharField("费用类型", choices=pay_type_choices, max_length=64, default="deposit")
    paid_fee = models.IntegerField("费用数额", default=0)
    note = models.TextField("备注",blank=True, null=True)
    date = models.DateTimeField("交款日期", auto_now_add=True)
    consultant = models.ForeignKey(UserProfile, verbose_name="负责老师", help_text="谁签的单就选谁", on_delete=models.CASCADE)

    def __str__(self):
        return "%s, 类型:%s,数额:%s" %(self.enrollment.customer, self.pay_type, self.paid_fee)

    class Meta:
        verbose_name = '交款纪录'
        verbose_name_plural = "交款纪录"
