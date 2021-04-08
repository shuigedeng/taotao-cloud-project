from django.contrib import admin
from web import models

class TaskAdmin(admin.ModelAdmin):
    list_display = ('id','task_type','content','date')

class TaskLogAdmin(admin.ModelAdmin):
    list_display = ('id','task','result','status','date')

admin.site.register(models.Host)
admin.site.register(models.HostGroup)
admin.site.register(models.IDC)
admin.site.register(models.UserProfile)
admin.site.register(models.HostToRemoteUser)
admin.site.register(models.RemoteUser)
admin.site.register(models.AuditLog)
admin.site.register(models.Task,TaskAdmin)
admin.site.register(models.TaskLogDetail,TaskLogAdmin)

# Register your models here.
