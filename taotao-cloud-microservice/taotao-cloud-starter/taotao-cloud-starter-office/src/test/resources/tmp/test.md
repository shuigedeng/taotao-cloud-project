### 一、前言

> **温馨小提示**: 这是测试数据

###### 基本环境

1. idea
2. CentOS7.3服务器
3. docker
4. springboot项目

### 二、idea使用docker部署项目

#### 1. idea安装docker插件

![在这里插入图片描述](https://img-blog.csdnimg.cn/20190822150005579.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM4MjI1NTU4,size_16,color_FFFFFF,t_70)

#### 2. 配置项目 `pom.xml` 文件
```xml
<properties>
    <docker.image.prefix>docker-demo</docker.image.prefix>
</properties>
<!-- ... -->
```
