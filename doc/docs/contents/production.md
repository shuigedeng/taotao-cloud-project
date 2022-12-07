

### windows安装git，管理页面

window上主要是日常博客的编写，然后用git来管理，上传到gitee后，可以配置生成GiteePages。具体的git操作和配置可以参考[git详解文章](https://blog.csdn.net/liyou123456789/article/details/121411053)，这里创建了一个gitee仓库，专门用来做个人网站。下面红框框出来的是docsify生成的几个配置文件。

### Gitee部署

#### Gitee配置Gitee Pages

第一次配置需要实名认证，上传身份证正反面，手持身份证照片。

部署选择你要部署的分支，部署的目录就是docsify对应仓库中的目录，我这边是整个仓库作为docsify目录，建议强制使用https勾选，然后就可以启动。

<div align=left><img src="https://librarycodes.gitee.io/docsify-plus/img/gtee_00.png" style="zoom:100%;" />
</div>


#### 页面效果请参考

生成的giteePages地址是https://bluecusliyou.gitee.io/techlearn



#### 页面修改更新

如果页面内容有修改更新到仓库了，可以点击更新个人页面

<div align=left><img src="https://librarycodes.gitee.io/docsify-plus/img/gtee_01.png" style="zoom:100%;" />
</div>

### GitHub部署

有三个地方可以为您的 GitHub 存储库填充文档：

- `docs/`文件夹
- 主分支
- gh-pages 分支

建议您将文件保存到存储库分支的`./docs`子文件夹中。`master`然后`master branch /docs folder`在存储库的设置页面中选择作为 GitHub Pages 源。


您也可以将文件保存在根目录中，然后选择`master branch`. 您需要`.nojekyll`在部署位置放置一个文件（例如`/docs`gh-pages 分支）



### GitLab 页面

如果要部署 master 分支，请`.gitlab-ci.yml`使用以下脚本创建一个：

`.public`解决方法是`cp`不会在无限循环中复制到`public/`自身。

```
pages:
  stage: deploy
  script:
  - mkdir .public
  - cp -r * .public
  - mv .public public
  artifacts:
    paths:
    - public
  only:
  - master
```

`- cp -r docs/. public`如果`./docs`是您的 Docsify 子文件夹，您可以用 替换脚本。

### Firebase 托管

使用 Google 帐户登录[Firebase 控制台](https://console.firebase.google.com/)`npm i -g firebase-tools`后，您需要安装 Firebase CLI 。

使用终端，确定并导航到 Firebase 项目的目录。这可能是`~/Projects/Docs`，等等。从那里，运行`firebase init`并`Hosting`从菜单中选择（使用**空格**键选择，**箭头键**更改选项并**输入**确认）。按照设置说明进行操作。

您的`firebase.json`文件应该类似于此（我将部署目录从 更改`public`为`site`）：

```json
{
  "hosting": {
    "public": "site",
    "ignore": ["firebase.json", "**/.*", "**/node_modules/**"]
  }
}
```

完成后，通过运行构建起始模板`docsify init ./site`（将站点替换为您在运行时确定的部署目录`firebase init`- 默认为公共）。添加/编辑文档，然后`firebase deploy`从根项目目录运行。

### nginx部署

使用以下 nginx 配置。

```nginx
server {
  listen 80;
  server_name  your.domain.com;

  location / {
    alias /path/to/dir/of/docs/;
    index index.html;
  }
}
```

### HTML5 路由器

使用 HTML5 路由器时，您需要设置重定向规则，将所有请求重定向到您的`index.html`. 当您使用 Netlify 时，这非常简单。`_redirects`只需在 docs 目录中创建一个名为的文件，将此代码段添加到文件中，就可以了：

```sh
/*    /index.html   200
```

### Docker部署

- 创建 docsify 文件

  需要准备初始文件，而不是在容器内制作它们。有关如何手动或使用[docsify-cli](https://github.com/docsifyjs/docsify-cli)创建这些文件的说明，请参阅[快速入门](https://docsify.js.org/#/quickstart)部分。

  ```sh
  index.html
  README.md
  ```

- 创建 Dockerfile

  ```dockerfile
    FROM node:latest
    LABEL description="A demo Dockerfile for build Docsify."
    WORKDIR /docs
    RUN npm install -g docsify-cli@latest
    EXPOSE 3000/tcp
    ENTRYPOINT docsify serve .
  ```

  当前目录结构应该是这样的：

  ```sh
   index.html
   README.md
   Dockerfile
  ```

- 构建 docker 镜像

  ```sh
  docker build -f Dockerfile -t docsify/demo .
  ```

- 运行 docker 镜像

  ```sh
  docker run -itp 3000:3000 --name=docsify -v $(pwd):/docs docsify/demo
  ```

  

