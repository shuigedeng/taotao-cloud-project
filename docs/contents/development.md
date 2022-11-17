### 安装node环境

安装`docsify-cli`之前，我们需要安装`npm`包管理器，而安装了`node.js`就会自动安装`npm`

#### 安装node

官网下载安装程序，双击下载的exe安装，下一步下一步直到完成。

官网地址：<https://nodejs.org/en/>

#### 验证安装

输入node -v，npm -v输出版本就是安装成功了

```
#验证node
node -v

#验证npm
npm -v
```

### 安装docsify-cli工具

推荐全局安装 `docsify-cli` 工具，可以方便地创建及在本地预览生成的文档。

```
#用npm安装全局工具
npm i docsify-cli -g
```

### 下载Docsify-Plus项目

```
Gitee地址：
https://gitee.com/librarycodes/docsify-plus

GitHub地址：
https://github.com/shiming-git/docsify-plus
```

### 运行Docsify-Plus

默认运行端口为 3000  docs为Docsify-Plus的默认目录，此目录可自行更改，可改为更目录

```
docsify serve docs
```

### 指定端口运行

-p 80  指定运行端口  为 80 

```
docsify serve -p 80 docs
```

### 本地预览

游览器打开输入访问地址，http://{你的ID地址}:{你指定的端口}

```
http://localhost:3000
```

