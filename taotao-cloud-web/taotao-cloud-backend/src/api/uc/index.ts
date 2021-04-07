import { UserParamQuery, UserVO } from './model'
import { getFetch } from '/@/http'
import { PageResult, Result } from '/@/api/model/baseModel'
import { IMenu } from '/@/config/menuConfig'

const uc = {
  queryUser(data: UserParamQuery): Promise<PageResult<UserVO>> {
    return getFetch<PageResult<UserVO>, UserParamQuery>(
      '/auth/oauth/token/user',
      data
    )
  },
  getLoginUserMenuInfo(): Promise<Result<IMenu[]>> {
    return getFetch<Result<IMenu[]>, any>('/uc/current/menus')
  },
  getLoginUserInfo(): Promise<Result<UserVO>> {
    return getFetch<Result<UserVO>, any>('/uc/current/user')
  },
  getLoginUserRouters(): Promise<
    Result<
      {
        path: string
        exact: boolean
        name: string
        component: string
        roles: string[]
      }[]
    >
  > {
    return getFetch<
      Result<
        {
          path: string
          exact: boolean
          name: string
          component: string
          roles: string[]
        }[]
      >,
      any
    >('/uc/current/routers')
  }
}

export default uc
