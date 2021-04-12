from django.db import models
from  crm import models as crm_models
# Create your models here.



class FlowTemplate(models.Model):
    """流程模版"""
    name = models.CharField(max_length=128, unique=True)
    description = models.TextField(blank=True, null=True)
    flow_type_choices = (('FlowVaction', '请假流程'),
                         ('FlowTrip', '出差申请'),
                         ('FlowBecome_Full_Staff', '转正申请'),
                         ('FlowLoan', '借款申请'),
                         )
    flow_type = models.CharField(choices=flow_type_choices, max_length=64)

    def __str__(self):
        return self.name


class Flow(models.Model):
    """流程总表，存储所有流程都会有的公共信息"""
    template = models.ForeignKey(FlowTemplate, on_delete=models.CASCADE)
    started_user = models.ForeignKey(crm_models.UserProfile,verbose_name="流程发起人",on_delete=models.CASCADE)
    content = models.TextField(blank=True,null=True,verbose_name="申请内容")
    date = models.DateTimeField(auto_created=True,auto_now=True)
    def __str__(self):
        return "%s 发起人:%s" %(self.template,self.started_user)

class FlowTrip(models.Model):
    """出差流程"""
    flow = models.ForeignKey("Flow",on_delete=models.CASCADE)
    start_date = models.DateTimeField("开始时间")
    end_date = models.DateTimeField("结束时间")

class FlowLoan(models.Model):
    """借款申请"""
    flow = models.ForeignKey("Flow",on_delete=models.CASCADE)
    usage_choices = ((0,'出差借款'),)
    usage = models.SmallIntegerField(choices=usage_choices,verbose_name="用途")
    amount = models.PositiveIntegerField("借款金额")
    start_date = models.DateTimeField("用款时间")
    end_date = models.DateTimeField("还款时间")


class FlowBecome_Full_Staff(models.Model):
    """转正流程"""
    flow = models.ForeignKey("Flow",on_delete=models.CASCADE)
    probation_start_date = models.DateField("入职日期")
    probation_end_date = models.DateField("转正日期")


class FlowVaction(models.Model):
    """请假流程"""
    flow = models.ForeignKey("Flow", on_delete=models.CASCADE)
    vaction_type_choices  = ((0,'病假'),(1,'年假'),(2,'事假'),(3,'产假'))
    vaction_type = models.SmallIntegerField(choices=vaction_type_choices,default=2)
    start_date = models.DateTimeField("开始时间")
    end_date = models.DateTimeField("结束时间")
    def __str__(self):
        return "%s: %s"%(self.flow,self.vaction_type)


class Step(models.Model):
    """流程的每个环节"""
    flow_template = models.ForeignKey("FlowTemplate",verbose_name="所属流程", on_delete=models.CASCADE)
    name = models.CharField("环节名称",max_length=128)
    description = models.TextField("环节介绍",blank=True,null=True)
    order = models.PositiveSmallIntegerField("环节步骤")
    role = models.ManyToManyField("FlowRole",verbose_name="审批角色")
    is_countersign =  models.BooleanField("会签环节",default=False)
    required_polls = models.PositiveSmallIntegerField("会签最少需同意的人数",blank=True,null=True)

    def __str__(self):
        return "流程:%s 名称:%s 环节:%s" %(self.flow_template,self.name,self.order)

    class Meta:
        unique_together = ("flow_template",'order')


class FlowRecord(models.Model):
    """流程的流转记录"""
    flow = models.ForeignKey("Flow",default=1, on_delete=models.CASCADE)
    step = models.ForeignKey("Step", on_delete=models.CASCADE)
    user = models.ForeignKey(crm_models.UserProfile, on_delete=models.CASCADE)
    status_choices = ((0,'同意'),(1,'拒绝'),(2,'需额外审批人审批'))
    status = models.SmallIntegerField(choices=status_choices,verbose_name="审批状态")
    comment = models.TextField(max_length=1024, verbose_name="审批意见")
    extra_parties = models.ManyToManyField(crm_models.UserProfile,verbose_name="额外审批人列表",
                                           related_name="related_parties",
                                           blank=True)
    date = models.DateTimeField(auto_now_add=True)


    def __str__(self):
        return "%s:%s" %(self.step,self.get_status_display())


class FlowRole(models.Model):
    """流程角色"""
    name = models.CharField(max_length=64,unique=True)
    users = models.ManyToManyField(crm_models.UserProfile)

    def __str__(self):
        return self.name

