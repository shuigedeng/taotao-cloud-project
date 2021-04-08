
from django.conf.urls import url,include
from crm import  views

urlpatterns = [
    url(r'^$', views.sales_dashboard, name="crm_dashboard"), #销售角色首页
    #url(r'^customers/$', views.customers, name="customers"),
    url(r'^my_customers/$', views.my_customers, name="my_customers"),
    url(r'^sales_report/$', views.sales_report, name="sales_report"),
    url(r'^enrollment/(\d+)/$', views.enrollment, name="enrollment"),
    url(r'^enrollment/stu/(\d+)/$', views.stu_enrollment, name="stu_enrollment"),
]
