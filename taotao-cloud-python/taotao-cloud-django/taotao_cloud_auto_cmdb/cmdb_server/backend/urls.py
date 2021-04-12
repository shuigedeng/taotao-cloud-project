"""cmdb_server URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/1.11/topics/http/urls/
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
from backend import views

urlpatterns = [
    url(r'^home.html/', views.home),    # 后台管理首页

    url(r'^show_server.html/', views.show_server),      # 服务器列表
    url(r'^server.html/', views.show_server_record),    # 服务器curd操作

    url(r'^show_asset.html/', views.show_asset),        # 资产列表
    url(r'^asset.html/', views.show_asset_record),      # 资产curd操作

    url(r'^show_idc.html/', views.show_idc),        # idc列表
    url(r'^idc.html/', views.show_idc_record),      # idc curd操作
]
