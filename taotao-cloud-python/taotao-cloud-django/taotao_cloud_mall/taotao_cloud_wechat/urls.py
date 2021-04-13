"""WebChat URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/1.11/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  url(r'$', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  url(r'$', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.conf.urls import url, include
    2. Add a URL to urlpatterns:  url(r'blog/', include('blog.urls'))
"""
from django.conf.urls import url
from werobot.contrib.django import make_view

from taotao_cloud_wechat import views, robot

urlpatterns = [
    # 微信公众号机器人
    url('robot/', make_view(robot.robot)),
    # 获取二维码
    url('qrcode/', views.login),
    # 长连接请求
    url('polling/', views.long_polling),
    # 登录成功跳转的页面
    url('index/', views.index),
    # 获取更多联系人列表
    url('contact_list/', views.contact_list),
    # 发送消息
    url('send_msg/', views.send_msg),
    # 获取消息
    url('get_msg/', views.get_msg),
]
