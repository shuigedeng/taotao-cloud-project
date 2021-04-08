
from django.conf.urls import url,include
from student import  views

urlpatterns = [
    url(r'^$', views.my_courses, name="my_courses"),
    url(r'my_grade/$', views.my_grade, name="my_grade"),
    url(r'course/(\d+)/homework/$', views.my_homeworks, name="my_homeworks"),
    url(r'course/(\d+)/homework/(\d+)/$', views.homework_detail, name="homework_detail"),
    url(r'course/(\d+)/homework/(\d+)/delete/$', views.delete_file, name="delete_file"),

]
