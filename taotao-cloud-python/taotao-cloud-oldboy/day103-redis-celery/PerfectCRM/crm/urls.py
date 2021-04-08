
from django.conf.urls import url,include
from crm import views



from rest_framework import routers
from crm.rest_views import  UserViewSet,RoleViewSet
# Routers provide an easy way of automatically determining the URL conf.
router = routers.DefaultRouter()
router.register(r'users', UserViewSet)
router.register(r'roles', RoleViewSet)




urlpatterns = [
    url(r'^api/', include(router.urls)),
    url(r'^$', views.dashboard,name="sales_dashboard"   ),
    url(r'^stu_enrollment/$', views.stu_enrollment,name="stu_enrollment"   ),
    url(r'^enrollment/(\d+)/$', views.enrollment,name="enrollment"   ),
    url(r'^enrollment/(\d+)/fileupload/$', views.enrollment_fileupload,name="enrollment_fileupload"   ),
    url(r'^stu_enrollment/(\d+)/contract_audit/$', views.contract_audit,name="contract_audit"   ),
    url(r'^apitest/', views.api_test  ),
    url(r'^celery_test/', views.celery_test  ),
    url(r'^celery_res/', views.celery_res  ),


]





