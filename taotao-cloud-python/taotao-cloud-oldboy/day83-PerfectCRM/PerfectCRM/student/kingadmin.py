
from kingadmin.sites import site
from student import models
print('student kingadmin ............')

class TestAdmin(object):
    list_display = ['name']

site.register(models.Test,TestAdmin)