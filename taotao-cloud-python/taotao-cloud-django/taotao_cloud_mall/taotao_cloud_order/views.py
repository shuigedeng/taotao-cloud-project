from datetime import datetime

from django.http import HttpResponse

from taotao_cloud_order import models as order_models
from taotao_cloud_uc import models as uc_models


def current_datetime(request):
    now = datetime.now()
    html = "<html><body>It is now %s.</body></html>" % now
    return HttpResponse(html)


def get_orders(request):
    course = order_models.Course.objects.all().values('name', 'desc')
    print(course)
    return HttpResponse(course)


def get_uc(request):
    userinfo = uc_models.UserInfo.objects.all().values('username', 'password')
    print(userinfo)
    return HttpResponse(userinfo)
