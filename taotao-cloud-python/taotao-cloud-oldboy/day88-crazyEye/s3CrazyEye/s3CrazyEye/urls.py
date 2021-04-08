"""s3CrazyEye URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/1.10/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  url(r'^$', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  url(r'^$', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.conf.urls import url, include
    2. Add a URL to urlpatterns:  url(r'^blog/', include('blog.urls'))
"""
from django.conf.urls import url
from django.contrib import admin
from web import views

urlpatterns = [
    url(r'^admin/', admin.site.urls),
    url(r'^$', views.dashboard),
    url(r'^login/$', views.acc_login),
    url(r'^web_ssh/$', views.web_ssh,name='web_ssh'),
    url(r'^host_mgr/cmd/$', views.host_mgr,name='batch_cmd'),
    url(r'^host_mgr/file_transfer/$', views.file_transfer,name='file_transfer'),
    url(r'^batch_task_mgr/$', views.batch_task_mgr,name='batch_task_mgr'),
    url(r'^task_result/$', views.task_result,name='get_task_result'),
]
