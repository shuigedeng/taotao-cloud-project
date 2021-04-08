from django.contrib import admin
from crm import models
# Register your models here.

class CustomerAdmin(admin.ModelAdmin):
    list_display = ['id','name','source','contact_type','contact','consultant','consult_content','status','date']
    list_filter = ['source','consultant','status','date']
    search_fields = ['contact','consultant__name']

    readonly_fields = ['status','contact']
    filter_horizontal = ['consult_courses',]

    actions = ['change_status',]
    def change_status(self,request,querysets):
        querysets.update(status=1)

class CourseRecordAdmin(admin.ModelAdmin):
    list_display = ['class_grade','day_num','has_homework']
    #list_per_page = 2
    list_editable = ['has_homework',]
admin.site.register(models.CustomerInfo,CustomerAdmin)
admin.site.register(models.CustomerFollowUp)
admin.site.register(models.ClassList)
admin.site.register(models.Course)
admin.site.register(models.Role)
admin.site.register(models.Menus)
admin.site.register(models.CourseRecord,CourseRecordAdmin)
admin.site.register(models.StudyRecord)
admin.site.register(models.Student)
admin.site.register(models.UserProfile)
admin.site.register(models.Branch)
admin.site.register(models.ContractTemplate)
admin.site.register(models.StudentEnrollment)
admin.site.register(models.PaymentRecord)
