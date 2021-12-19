declare module '*.css'
declare module '*.less'
declare module '*.scss'
declare module '*.sass'
declare module '*.svg'
declare module '*.png'
declare module '*.jpg'
declare module '*.jpeg'
declare module '*.gif'
declare module '*.bmp'
declare module '*.tiff'

declare const APP_PAGE_TITLE: string
declare const APP_ROUTER_MODE: 'hash' | 'history'

type RouteItem = {
  /** 显示在侧边栏的名称 */
  title: string
  /** 页面路由组件 */
  component?: React.LazyExoticComponent<() => JSX.Element> | JSX.Element
  /** 图标 */
  icon?: any
  /** 路径 */
  path?: string
  /** 子菜单路由 */
  children?: RouteItem[]
  /** 权限列表 */
  permissions?: string[]
  /** 控制是否在侧边栏中显示 */
  hidden?: boolean
  key?: string
}

type RouteArray<T> = T[] | RouteArray<T>[]

type Routes = RouteArray<RouteItem>
