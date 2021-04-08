
from django.conf.urls import url,include
from crm import kingadmin_views

urlpatterns = [

    # url(r'^$', kingadmin_views.configure_index, name="table_index"),  # 显示所有注册的表
    # url(r'^(\w+)/$',kingadmin_views.configure_url_dispatch,name="table_list"), #显示每个表的数据
    # url(r'^(\w+)/add/$', kingadmin_views.table_add, name="table_add"),
    # url(r'^(\w+)/change/(\d+)/$', kingadmin_views.table_change, name="table_change"),
    # url(r'^(\w+)/change/(\d+)/password/$', kingadmin_views.password_reset_form),
    #
    # url(r'^(\w+)/delete/(\d+)/$', kingadmin_views.table_del, name="table_del"),

]
