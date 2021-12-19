<p align="center">
  <img src="https://cdn.mate.vip/matecloud.svg" width="260">
</p>
<p align="center">
  <img src='https://img.shields.io/github/license/matevip/matecloud' alt='License'/>
  <img src="https://img.shields.io/github/stars/matevip/artemis" alt="Stars"/>
  <img src="https://img.shields.io/badge/VUE-3.2.2-green" alt="VUE"/>
  <img src="https://img.shields.io/badge/VueRouter-4.0.11-blue" alt="vue-router4"/>
  <img src="https://img.shields.io/badge/Vite-2.5.0-brightgreen" alt="Vite"/>
</p>


# `Artemis Admin` 基于Vue3的中后台系统

## 🔥 当前版本
`4.0.8-M2` 基础功能已完善，后续功能正在加紧迭代开发中，欢迎交流


## 💡 简介

Artemis Admin 是基于vben的模板开发，使用了最新的`vue3`,`vite2`,`TypeScript`等主流技术，开箱即用的中后台前端解决方案，完全开源，可以商用。

## 💥 特性

- **最新技术栈**：使用 Vue3/vite2 等前端前沿技术开发
- **TypeScript**: 应用程序级 JavaScript 的语言
- **主题**：可配置的主题
- **国际化**：内置完善的国际化方案
- **Mock 数据** 内置 Mock 数据方案
- **权限** 内置完善的动态路由权限生成方案
- **组件** 二次封装了多个常用的组件

## 🎨 系统演示
### 👉 演示地址：http://cloud.mate.vip

账号 | 密码| 操作权限
---|---|---
admin | matecloud| mate-system模块不能执行增删改请求

如果需要验证手机号码登录，手机号码采用页面默认号码，点击获取验证码，输入1188，即可登录。
### 🍯 企业版：http://plus.mate.vip
账号 | 密码| 操作权限
---|---|---
admin | matecloud123 | 不能执行增删改请求，如需全部权限加微信 matecloud 联系


<table>
  <tr>
    <td>
    <img alt="artemis admin" width="100%" src="https://gitee.com/matevip/matecloud/raw/dev/doc/images/artemis_page1.png">
    </td>
     <td>
    <img alt="artemis admin" width="100%" src="https://gitee.com/matevip/matecloud/raw/dev/doc/images/artemis_page2.png">
    </td>

  </tr>
  <tr>
    <td>
    <img alt="artemis admin" width="100%" src="https://gitee.com/matevip/matecloud/raw/dev/doc/images/artemis_page3.png">
    </td>
    <td>
    <img alt="artemis admin" width="100%" src="https://gitee.com/matevip/matecloud/raw/dev/doc/images/artemis_page4.png">
    </td>
  </tr>
  <tr>
    <td>
    <img alt="artemis admin" width="100%" src="https://gitee.com/matevip/matecloud/raw/dev/doc/images/artemis_page5.png">
    </td>
    <td>
    <img alt="artemis admin" width="100%" src="https://gitee.com/matevip/matecloud/raw/dev/doc/images/artemis_page6.png">
    </td>
  </tr>
</table>


## 🌐 文档

[文档地址](http://doc.mate.vip/)

## 🔧 准备

- [node](http://nodejs.org/) 和 [git](https://git-scm.com/) -项目开发环境
- [Vite](https://vitejs.dev/) - 熟悉 vite 特性
- [Vue3](https://v3.vuejs.org/) - 熟悉 Vue 基础语法
- [TypeScript](https://www.typescriptlang.org/) - 熟悉`TypeScript`基本语法
- [Es6+](http://es6.ruanyifeng.com/) - 熟悉 es6 基本语法
- [Vue-Router-Next](https://next.router.vuejs.org/) - 熟悉 vue-router 基本使用
- [Ant-Design-Vue](https://2x.antdv.com/docs/vue/introduce-cn/) - ui 基本使用
- [Mock.js](https://github.com/nuysoft/Mock) - mockjs 基本语法

## 🔨 安装使用

- 获取项目代码

```bash
git clone https://gitee.com/matevip/artemis.git
```

- 安装依赖

```bash
cd artemis

yarn install

```

- 运行

```bash
yarn serve
```

- 打包

```bash
yarn build
```


## 🌭 项目地址
|  项目   |   GITHUB  |   码云   |
|---  |--- | --- |
|  MateCloud后端源码   |  https://github.com/matevip/matecloud   |  https://gitee.com/matevip/matecloud   |
|  Artemis前端源码   |  https://github.com/matevip/artemis   |  https://gitee.com/matevip/artemis   |
|  MateBoot后端源码   |  https://github.com/matevip/mateboot   |  https://gitee.com/matevip/mateboot   |

## 🔨 如何贡献

非常欢迎你的加入！[提一个 Issue](https://gitee.com/matevip/artemis/issues) 或者提交一个 Pull Request。

**Pull Request:**

1. Fork 代码!
2. 创建自己的分支: `git checkout -b feat/xxxx`
3. 提交你的修改: `git commit -am 'feat(function): add xxxxx'`
4. 推送您的分支: `git push origin feat/xxxx`
5. 提交`pull request`

## Git 贡献提交规范

- 参考 [vue](https://github.com/vuejs/vue/blob/dev/.github/COMMIT_CONVENTION.md) 规范 ([Angular](https://github.com/conventional-changelog/conventional-changelog/tree/master/packages/conventional-changelog-angular))

  - `feat` 增加新功能
  - `fix` 修复问题/BUG
  - `style` 代码风格相关无影响运行结果的
  - `perf` 优化/性能提升
  - `refactor` 重构
  - `revert` 撤销修改
  - `test` 测试相关
  - `docs` 文档/注释
  - `chore` 依赖更新/脚手架配置修改等
  - `workflow` 工作流改进
  - `ci` 持续集成
  - `types` 类型定义文件更改
  - `wip` 开发中

## 浏览器支持

本地开发推荐使用`Chrome 80+` 浏览器

支持现代浏览器, 不支持 IE

| [<img src="https://raw.githubusercontent.com/alrra/browser-logos/master/src/edge/edge_48x48.png" alt=" Edge" width="24px" height="24px" />](http://godban.github.io/browsers-support-badges/)</br>IE | [<img src="https://raw.githubusercontent.com/alrra/browser-logos/master/src/edge/edge_48x48.png" alt=" Edge" width="24px" height="24px" />](http://godban.github.io/browsers-support-badges/)</br>Edge | [<img src="https://raw.githubusercontent.com/alrra/browser-logos/master/src/firefox/firefox_48x48.png" alt="Firefox" width="24px" height="24px" />](http://godban.github.io/browsers-support-badges/)</br>Firefox | [<img src="https://raw.githubusercontent.com/alrra/browser-logos/master/src/chrome/chrome_48x48.png" alt="Chrome" width="24px" height="24px" />](http://godban.github.io/browsers-support-badges/)</br>Chrome | [<img src="https://raw.githubusercontent.com/alrra/browser-logos/master/src/safari/safari_48x48.png" alt="Safari" width="24px" height="24px" />](http://godban.github.io/browsers-support-badges/)</br>Safari |
| :-: | :-: | :-: | :-: | :-: |
| not support | last 2 versions | last 2 versions | last 2 versions | last 2 versions |

## ✨ 特别鸣谢
特别感谢卢神对`MateCloud`项目提供的技术支持！
- 卢春梦: [mica](https://gitee.com/596392912/mica)  
- chuzhixin: [vue-admin-beautiful](https://github.com/chuzhixin/vue-admin-beautiful)
- anncwb: [vue-vben-admin](https://github.com/anncwb/vue-vben-admin)
