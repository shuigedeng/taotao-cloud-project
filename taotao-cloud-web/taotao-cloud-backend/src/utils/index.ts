import { isObject } from '/@/utils/is'
import { IMenu } from '/@/config/menuConfig'

export const timestamp = () => +Date.now()
export const clamp = (n: number, min: number, max: number) =>
  Math.min(max, Math.max(min, n))
export const noop = () => {}
export const now = () => Date.now()

/**
 * @description:  Set ui mount node
 */
export function getPopupContainer(node?: HTMLElement): HTMLElement {
  if (node) {
    return node.parentNode as HTMLElement
  }
  return document.body
}

/**
 * Add the object as a parameter to the URL
 * @param baseUrl url
 * @param obj
 * @returns {string}
 * eg:
 *  let obj = {a: '3', b: '4'}
 *  setObjToUrlParams('www.baidu.com', obj)
 *  ==>www.baidu.com?a=3&b=4
 */
export function setObjToUrlParams(baseUrl: string, obj: any): string {
  let parameters = ''
  let url = ''
  for (const key in obj) {
    parameters += key + '=' + encodeURIComponent(obj[key]) + '&'
  }
  parameters = parameters.replace(/&$/, '')
  if (/\?$/.test(baseUrl)) {
    url = baseUrl + parameters
  } else {
    url = baseUrl.replace(/\/?$/, '?') + parameters
  }
  return url
}

export function deepMerge<T = any>(src: any, target: any): T {
  let key: string
  for (key in target) {
    src[key] = isObject(src[key])
      ? deepMerge(src[key], target[key])
      : (src[key] = target[key])
  }
  return src
}

/**
 * @description: 根据数组中某个对象值去重
 */
export function unique<T = any>(arr: T[], key: string): T[] {
  const map = new Map()
  return arr.filter(item => {
    const _item = item as any
    return !map.has(_item[key]) && map.set(_item[key], 1)
  })
}

/**
 * @description: es6数组去重复
 */
export function es6Unique<T>(arr: T[]): T[] {
  return Array.from(new Set(arr))
}

export function openWindow(
  url: string,
  opt?: {
    target?: any | string
    noopener?: boolean
    noreferrer?: boolean
  }
) {
  const { target = '__blank', noopener = true, noreferrer = true } = opt || {}
  const feature: string[] = []

  noopener && feature.push('noopener=yes')
  noreferrer && feature.push('noreferrer=yes')

  window.open(url, target, feature.join(','))
}

/**
 * 根据用户权限 获取 对应权限菜单
 * @param roles 用户的权限
 * @param routes 框架对应路由
 */
export const getPermissionMenuData = (
  roles: string[],
  routes: IMenu[]
): IMenu[] => {
  const menu: IMenu[] = []
  for (let index = 0, len = routes.length; index < len; index += 1) {
    const element = routes[index]
    if (hasPermission(roles, element)) {
      if (element.children) {
        element.children = getPermissionMenuData(roles, element.children)
      }
      menu.push(element)
    }
  }

  return menu
}

/**
 * 根据 route.roles 判断当前用户是否有权限
 * @param roles 用户的权限
 * @param route 当前路由
 */
export const hasPermission = (roles: string[], route: IMenu): boolean => {
  if (roles.includes('admin')) {
    return true
  }

  if (route.roles) {
    return route.roles.some(role => roles.includes(role))
  }

  return true
}

// 根据某个属性值从MenuList查找拥有该属性值的menuItem
export function getMenuItemInMenuListByProperty(
  menuList: IMenu[],
  value: string | number
): IMenu {
  let stack: IMenu[] = []
  stack = stack.concat(menuList)
  let res: IMenu

  while (stack.length) {
    const cur = stack.shift()
    if (cur?.children && cur.children.length > 0) {
      stack = cur.children.concat(stack)
    }
    if (cur) {
      if (cur['path'] === value) {
        res = cur
      }
    }
  }
  // @ts-ignore
  return res
}
