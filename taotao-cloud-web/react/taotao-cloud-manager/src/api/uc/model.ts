import { BasicPageParams } from '/@/api/model/baseModel'
import { IMenu } from '/@/config/menuConfig'
import { IRoutes } from '/@/config/routeConfig'

export type UserVO = {
  id: number
  nickname: string
  username: string
  phone: string
  type: number
  sex: number
  email: string
  deptId: number
  jobId: number
  avatar: string
  lockFlag?: number
  roles: Array<string>
  permissions: Array<string>
  createTime: string
  menus: IMenu[]
  routers: IRoutes[]
  lastModifiedTime: string
}

export interface UserParamQuery extends BasicPageParams {
  id: number
  name: string
}
