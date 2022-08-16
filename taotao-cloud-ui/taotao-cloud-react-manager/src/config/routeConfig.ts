import React from 'react'
// import loadableUtil from "../utils/loadableUtil"
import { RouteComponentProps } from 'react-router'

// const Dashboard = loadableUtil(() => import('../pages/dashboard'))
//
// // // 通用
// const ButtonView = loadableUtil(() => import(/* webpackChunkName: 'button' */ '/@/pages/general/button/ButtonView'))
// // const IconView = loadableUtil(() => import(/* webpackChunkName: 'icon' */ '/@/views/PublicView/Index'))
// //
// // 导航
// const DropdownView = loadableUtil(() => import(/* webpackChunkName: 'dropdown' */ '/@/pages/general/button/ButtonView'))
// const MenuView = loadableUtil(() => import(/* webpackChunkName: 'menu' */ '/@/pages/general/button/ButtonView'))
// const StepView = loadableUtil(() => import(/* webpackChunkName: 'step' */ '/@/views/NavView/Step'))
//
// // 表单
// const FormBaseView = loadableUtil(() => import(/* webpackChunkName: 'formBase' */ '/@/views/FormView/FormBaseView'))
// const FormStepView = loadableUtil(() => import(/* webpackChunkName: 'formStep' */ '/@/views/FormView/FormStepView'))
//
// // 展示
// const TableView = loadableUtil(() => import(/* webpackChunkName: 'table' */ '/@/views/ShowView/Table'))
// const CollapseView = loadableUtil(() => import(/* webpackChunkName: 'collapse' */ '/@/views/ShowView/Collapse'))
// const TreeView = loadableUtil(() => import(/* webpackChunkName: 'tree' */ '/@/views/ShowView/Tree'))
// const TabsView = loadableUtil(() => import(/* webpackChunkName: 'tabs' */ '/@/views/ShowView/Tabs'))
//
// // 其它
// const ProgressView = loadableUtil(() => import(/* webpackChunkName: 'progress' */ '/@/views/Others/Progress'))
// const AnimationView = loadableUtil(() => import(/* webpackChunkName: 'animation' */ '/@/views/Others/Animation'))
// const EditorView = loadableUtil(() => import(/* webpackChunkName: 'editor' */ '/@/views/Others/Editor'))
// const UploadView = loadableUtil(() => import(/* webpackChunkName: 'upload' */ '/@/views/Others/Upload'))
//
// const Three = loadableUtil(() => import(/* webpackChunkName: 'three' */ '/@/views/TestView'))
// const About = loadableUtil(() => import(/* webpackChunkName: 'about' */ '/@/views/About'))

export interface IRoutes {
  path: string
  exact: boolean
  name: string
  component:
    | React.ComponentType<RouteComponentProps<any>>
    | React.ComponentType<any>
  roles: string[]
}

const routes: IRoutes[] = [
  // {path: '/dashboard', exact: true, name: '面板', component: Dashboard, roles: ['admin', 'order']},
  // {path: '/public/button', exact: false, name: '按钮', component: ButtonView, roles: ['admin', 'order']},
  // // { path: '/public/icon', exact: false, name: '图标', component: IconView, auth: [1] },
  // {path: '/nav/dropdown', exact: true, name: '下拉菜单', component: DropdownView, roles: ['admin', 'order']},
  // {path: '/nav/menu', exact: false, name: '导航菜单', component: MenuView, roles: ['admin', 'order']},
  // { path: '/nav/steps', true: false, name: '步骤条', component: StepView },
  // { path: '/form/base-form', exact: false, name: '表单', component: FormBaseView },
  // { path: '/form/step-form', exact: false, name: '表单', component: FormStepView },
  // { path: '/show/table', exact: false, name: '表格', component: TableView },
  // { path: '/show/collapse', exact: false, name: '折叠面板', component: CollapseView },
  // { path: '/show/tree', exact: false, name: '树形控件', component: TreeView },
  // { path: '/show/tabs', exact: false, name: '标签页', component: TabsView },
  // { path: '/others/progress', exact: false, name: '进度条', component: ProgressView, auth: [1] },
  // { path: '/others/animation', exact: false, name: '动画', component: AnimationView, auth: [1] },
  // { path: '/others/editor', exact: false, name: '富文本', component: EditorView, auth: [1] },
  // { path: '/others/upload', exact: false, name: '上传', component: UploadView, auth: [1] },
  // { path: '/one/two/three', exact: false, name: '三级', component: Three },
  // { path: '/about', exact: false, name: '关于', component: About, auth: [1] }
]

export default routes

export const routerMap = {
  // 'Dashboard': Dashboard,
  // 'ButtonView': ButtonView,
  // 'DropdownView': DropdownView,
  // 'MenuView': MenuView
}

export const filterRouterMap = (
  name: string
): React.ComponentType<RouteComponentProps<any>> | React.ComponentType<any> => {
  // @ts-ignore
  return routerMap[name]
}
