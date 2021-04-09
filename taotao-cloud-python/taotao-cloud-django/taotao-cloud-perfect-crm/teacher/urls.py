
from django.conf.urls import url,include
from teacher import  views

urlpatterns = [
    url(r'^$', views.dashboard, name="teacher_dashboard"),
    url(r'^my_classes/$', views.my_classes, name="my_classes"),
    url(r'^my_classes/(\d+)/stu_list/$', views.view_class_stu_list, name="view_class_stu_list"),

]
