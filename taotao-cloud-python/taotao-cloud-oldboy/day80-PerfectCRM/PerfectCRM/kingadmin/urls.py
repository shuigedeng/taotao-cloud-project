
from django.conf.urls import url,include
from django.contrib import admin
from kingadmin import views
urlpatterns = [

    url(r'^$', views.app_index, name="app_index"),
    url(r'^/(\w+)/(\w+)/$', views.table_obj_list, name="table_obj_list"),

    url(r'^login/',views.acc_login ),
    url(r'^logout/',views.acc_logout,name="logout" ),


]
