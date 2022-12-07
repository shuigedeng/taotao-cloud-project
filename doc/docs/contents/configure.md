### Docsify-Plus配置

`window.$docsify`您可以通过定义为对象来配置 Docsify ：

```html
<script>
  window.$docsify = {
    repo: 'docsifyjs/docsify',
    maxLevel: 3,
    coverpage: true,
  };
</script>
```

配置也可以定义为一个函数，在这种情况下，第一个参数是 Docsify`vm`实例。该函数应返回一个配置对象。`vm`这对于在诸如降价配置之类的地方进行引用很有用：

```html
<script>
  window.$docsify = function(vm) {
    return {
      markdown: {
        renderer: {
          code(code, lang) {
            // ... use `vm` ...
          },
        },
      },
    };
  };
</script>
```

### el

- 类型：`String`
- 默认：`#app`

始化时要挂载的 DOM 元素。它可以是 CSS 选择器字符串或实际的[HTMLElement](https://developer.mozilla.org/en-US/docs/Web/API/HTMLElement)。

```javascript
window.$docsify = {
  el: '#app',
};
```

### 章鱼图标跳转

- 类型：`String`
- 默认：`null`

置存储库 url 或字符串，以在站点的右上角`username/repo`添加[GitHub Corner小部件。](http://tholman.com/github-corners/)

```javascript
window.$docsify = {
  repo: 'docsifyjs/docsify',
  // or
  repo: 'https://github.com/docsifyjs/docsify/',
};
```

### 导航栏

- 类型：`Boolean|String`
- 默认：`false`

`_navbar.md`如果为**true**则从 Markdown 文件加载导航栏，否则从指定的路径加载它。

```javascript
window.$docsify = {
  // load from _navbar.md
  loadNavbar: true,

  // load from nav.md
  loadNavbar: 'nav.md',
};
```

### 侧边栏

- 类型：`Boolean|String`
- 默认：`false`

`_sidebar.md`如果为**true**则从 Markdown 文件加载侧边栏，否则从指定的路径加载它。

```javascript
window.$docsify = {
  // load from _sidebar.md
  loadSidebar: true,

  // load from summary.md
  loadSidebar: 'summary.md',
};
```

### 自动顶部

- 类型：`Boolean`
- 默认：`false`

更改路线时滚动到屏幕顶部。

```javascript
window.$docsify = {
  auto2top: true,
};
```

### 主页

- 类型：`String`
- 默认：`README.md`

`README.md`在您的 docs 文件夹中将被视为您网站的主页，但有时您可能需要提供另一个文件作为您的主页。

```javascript
window.$docsify = {
  // Change to /home.md
  homepage: 'home.md',

  // Or use the readme in your repo
  homepage:
    'https://raw.githubusercontent.com/docsifyjs/docsify/master/README.md',
};
```

如果您在侧边栏中有指向主页的链接，并希望在访问根 url 时将其显示为活动状态，请确保相应地更新侧边栏：

```
- Sidebar
  - [Home](/)
  - [Another page](another.md)
```

### 基本路径

- 类型：`String`

网站的基本路径。您可以将其设置为另一个目录或另一个域名。

```javascript
window.$docsify = {
  basePath: '/path/',

  // Load the files from another site
  basePath: 'https://docsify.js.org/',

  // Even can load files from other repo
  basePath:
    'https://raw.githubusercontent.com/ryanmcdermott/clean-code-javascript/master/',
};
```

### 相对路径

- 类型：`Boolean`
- 默认：`false`

如果为**true**，则链接相对于当前上下文。

例如目录结构如下：

```text
.
└── docs
    ├── README.md
    ├── guide.md
    └── zh-cn
        ├── README.md
        ├── guide.md
        └── config
            └── example.md
```

**启用**相对路径和当前 URL`http://domain.com/zh-cn/README`后，给定的链接将解析为：

```
guide.md              => http://domain.com/zh-cn/guide
config/example.md     => http://domain.com/zh-cn/config/example
../README.md          => http://domain.com/README
/README.md            => http://domain.com/README
```

```javascript
window.$docsify = {
  // Relative path enabled
  relativePath: true,

  // Relative path disabled (default value)
  relativePath: false,
};
```

### 封面

- 类型：`Boolean|String|String[]|Object`
- 默认：`false`

激活[封面功能](https://docsify.js.org/#/cover)。如果为真，它将从`_coverpage.md`.

```js
window.$docsify = {
  coverpage: true,

  // Custom file name
  coverpage: 'cover.md',

  // multiple covers
  coverpage: ['/', '/zh-cn/'],

  // multiple covers and custom file name
  coverpage: {
    '/': 'cover.md',
    '/zh-cn/': 'cover.md',
  },
};
```

### 网站徽标

- 类型：`String`

显示在侧边栏中的网站徽标。您可以使用 CSS 调整其大小。

```javascript
window.$docsify = {
  logo: '/_media/icon.svg',
};
```

### 网站名称

- 类型：`String`

显示在侧边栏中的网站名称。

```javascript
window.$docsify = {
  name: 'docsify',
};
```

名称字段还可以包含自定义 HTML，以便于自定义：

```javascript
window.$docsify = {
  name: '<span>docsify</span>',
};
```

### 名称链接

- 类型：`String`
- 默认：`window.location.pathname`

网站`name`链接到的 URL。

```javascript
window.$docsify = {
  nameLink: '/',

  // For each route
  nameLink: {
    '/zh-cn/': '#/zh-cn/',
    '/': '#/',
  },
};
```

### 主题颜色

- 类型：`String`

自定义主题颜色。在旧版浏览器中使用[CSS3 变量](https://developer.mozilla.org/en-US/docs/Web/CSS/Using_CSS_variables)功能和 polyfill。

```javascript
window.$docsify = {
  themeColor: '#3F51B5',
};
```

### 自动页眉

- 类型：`Boolean`

如果`loadSidebar`和`autoHeader`都启用，对于 中的每个链接`_sidebar.md`，在将其转换为 HTML 之前，在页面前添加一个标题。

```javascript
window.$docsify = {
  loadSidebar: true,
  autoHeader: true,
};
```

### 执行脚本

- 类型：`Boolean`

在页面上执行脚本。只解析第一个脚本标签（[demo](https://docsify.js.org/#/themes)）。如果存在 Vue，则默认情况下它是打开的。

```javascript
window.$docsify = {
  executeScript: true,
};
```

```html
## This is test

<script>
  console.log(2333)
</script>
```

### 无表情符号

- 类型：`Boolean`

禁用表情符号解析。

```javascript
window.$docsify = {
  noEmoji: true,
};
```

### 显示文件更新日期

- 类型：`String|Function`

我们可以通过**{docsify-updated }**变量显示文件更新日期。并将其格式化为`formatUpdated`

```javascript
window.$docsify = {
  formatUpdated: '{MM}/{DD} {HH}:{mm}',

  formatUpdated: function(time) {
    // ...

    return time;
  },
};
```

### 外部链接目标

- 类型：`String`
- 默认：`_blank`

目标是在 markdown 中打开外部链接。默认`'_blank'`（新窗口/标签）

```javascript
window.$docsify = {
  externalLinkTarget: '_self', // default: '_blank'
};
```

### 角落外部链接目标

- 类型：`String`
- 默认：`_blank`

目标是在右上角打开外部链接。默认`'_blank'`（新窗口/标签）

```javascript
window.$docsify = {
  cornerExternalLinkTarget: '_self', // default: '_blank'
};
```

### 外部链接关系

- 类型：`String`
- 默认：`noopener`

默认`'noopener'`（无开启程序）阻止新打开的外部页面（当[externalLinkTarget](https://docsify.js.org/#/configuration?id=externallinktarget)为时`'_blank'`）具有控制我们页面的能力。不`rel`设置时设置为否`'_blank'`。

```javascript
window.$docsify = {
  externalLinkRel: '', // default: 'noopener'
};
```

### 路由器模式

- 类型：`String`
- 默认：`hash`

```javascript
window.$docsify = {
	routerMode: 'history', // default: 'hash'
};
```

### 封面

- 类型：`Boolean`

问主页时仅加载封面。

```javascript
window.$docsify = {
  onlyCover: false,
};
```

### 未找到页面

- 类型：`Boolean`| `String`|`Object`

加载`_404.md`文件：

```javascript
window.$docsify = {
  notFoundPage: true,
};
```

### 上边距

- 类型：`Number`
- 默认：`0`

滚动内容页面以到达所选部分时，在顶部添加一个空格。*如果您有一个粘性标题*布局并且您想要将锚对齐到标题的末尾，这很有用。

```javascript
window.$docsify = {
  topMargin: 90, // default: 0
};
```

### 图片

#### 调整大小

```md
![logo](https://docsify.js.org/_media/icon.svg ':size=WIDTHxHEIGHT')
![logo](https://docsify.js.org/_media/icon.svg ':size=50x100')
![logo](https://docsify.js.org/_media/icon.svg ':size=100')

<!-- Support percentage -->

![logo](https://docsify.js.org/_media/icon.svg ':size=10%')
```

#### 自定义类

```md
![logo](https://docsify.js.org/_media/icon.svg ':class=someCssClass')
```

#### 自定义 ID

```md
![logo](https://docsify.js.org/_media/icon.svg ':id=someCssId')
```

#### 自定义标题 ID

```md
### Hello, world! :id=hello-world
```

### html标签中的Markdown

您需要在 html 和 markdown 内容之间插入一个空格。这对于在 details 元素中呈现 markdown 内容很有用。

```markdown
<details>
<summary>Self-assessment (Click to expand)</summary>

- Abc
- Abc

</details>
```

Markdown 内容也可以包装在 html 标签中。

```html
<div style='color: red'>

- listitem
- listitem
- listitem

</div>
```

