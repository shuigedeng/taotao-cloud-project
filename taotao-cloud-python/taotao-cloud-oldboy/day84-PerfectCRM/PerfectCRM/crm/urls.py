
from django.conf.urls import url,include
from crm import views

urlpatterns = [

    url(r'^$', views.dashboard,name="sales_dashboard"   ),
    url(r'^stu_enrollment/$', views.stu_enrollment,name="stu_enrollment"   ),
    url(r'^enrollment/(\d+)/$', views.enrollment,name="enrollment"   ),
    url(r'^enrollment/(\d+)/fileupload/$', views.enrollment_fileupload,name="enrollment_fileupload"   ),
    url(r'^stu_enrollment/(\d+)/contract_audit/$', views.contract_audit,name="contract_audit"   ),


]
