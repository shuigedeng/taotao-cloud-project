from django.contrib import admin

from . import models
admin.site.register(models.Trouble)
admin.site.register(models.UserInfo)