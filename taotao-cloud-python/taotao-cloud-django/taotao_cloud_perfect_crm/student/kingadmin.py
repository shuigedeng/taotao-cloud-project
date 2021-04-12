#_*_coding:utf-8_*_


from kingadmin.admin_base import BaseKingAdmin,site

from student import models


class StuAccountAdmin(BaseKingAdmin):
    list_display = ('account','profile')



site.register(models.Account, StuAccountAdmin)