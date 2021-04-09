
from django.conf.urls import url,include

from beeflow import views


urlpatterns = [

    url(r'^my_application/$', views.my_application,name='my_application'),
]
