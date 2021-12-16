export interface UserAction {
  type: UserActionType
  payload: UserActionPayload
}

export interface UserActionPayload {
  id?: number
  nickname?: string
  username?: string
  phone?: string
  type?: number
  sex?: number
  email?: string
  deptId?: number
  jobId?: number
  avatar?: string
  lockFlag?: number
  roles?: Array<string>
  permissions?: Array<string>
  createTime?: string
  lastModifiedTime?: string
  token?: string
}

export enum UserActionType {
  USER_SET_LOGIN_SUCCESS = 'setUserLoginSuccess',
  USER_SET_LOGIN_TOKEN = 'setUserLoginToken',
  USER_RESET_USER = 'resetUser'
}
