## Nginx location 优先级

Nginx loation 指令功能是允许根据用户请求的URI来匹配定义的各 location， 匹配到时，此请求将被相应的 location 配置模块中的配置所处理。

一句话总结就是：匹配用户访问的 URI 来做相应的处理。

配置格式：
```
Syntax:	
	location [ = | ~ | ~* | ^~ ] uri { ... }

    Example:
        location / {
            [ configuration ]
        }
```

匹配 URI 有以下几种修饰符方式：
```
    location = /uri {...} : 精确匹配检查。如果找到完全匹配，则搜索结束；
    location ^~ /uri {...} : URI前缀匹配，找到即停止，不支持正则表达式；
    location ~* /uri {...} : 正则表达式模式匹配检查，不区分大小写；
    location ~ /uri {...} : 正则表达式模式匹配检查，区分字符大小写；
    location /uri {...} : 普通路径前缀匹配；
```

各修饰符有优先级之分，上文从上到下优先级依次从高到低。

** 示例： ** 
```
location = / {
    [ configuration A ]
}

location / {
    [ configuration B ]
}

location /documents/ {
    [ configuration C ]
}

location ^~ /images/ {
    [ configuration D ]
}

location ~* \.(gif|jpg|jpeg)$ {
    [ configuration E ]
}
```

“/”请求将与配置A匹配，“/index.html”请求将匹配配置B，“/documents/document.html”请求将匹配配置C，“/images/1.gif”请求将匹配配置D，并且“/documents/1.jpg”请求将匹配配置E。

（完）

