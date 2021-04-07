import React from 'react'
import { getLoginUserToken } from '/@/utils/lsUtil'
import { UserAction } from './UserAction'

export const userInitState: IUserState = {
  id: 0,
  nickname: '',
  username: '',
  phone: '',
  type: 0,
  sex: 0,
  email: '',
  deptId: 0,
  jobId: 0,
  avatar: '',
  lockFlag: 0,
  roles: [],
  permissions: [],
  createTime: '',
  lastModifiedTime: '',
  token: getLoginUserToken(),
  userDispatch: () => {}
}

export interface IUserState {
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
  lockFlag: number
  roles: Array<string>
  permissions: Array<string>
  createTime: string
  lastModifiedTime: string
  token: string
  userDispatch: React.Dispatch<UserAction>
}
