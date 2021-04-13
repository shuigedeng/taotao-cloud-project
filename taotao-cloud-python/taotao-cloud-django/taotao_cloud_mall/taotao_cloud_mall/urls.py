"""taotao_cloud_mall URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/3.2/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""
from django.conf.urls import include
from django.contrib import admin
from django.urls import path

urlpatterns = [
    path('admin/', admin.site.urls),
    path('wechat/', include("taotao_cloud_wechat.urls")),
    path('uc/', include("taotao_cloud_uc.urls")),
    path('order/', include("taotao_cloud_order.urls")),
    path('oauth2/', include("taotao_cloud_oauth2.urls")),
    path('product/', include("taotao_cloud_product.urls")),
]
