export const userInitState: IUserState = {
  token: {
    access_token: '',
    expires_in: 0,
    refresh_token: '',
    scope: '',
    token_type: '',
    deptId: '',
    mobile: '',
    userId: 0,
    username: '',
  }
}

export interface IUserState {
  token: IOauth2Token,
  userDispatch?: any
}

export interface IOauth2Token {
  access_token: string
  expires_in: number
  refresh_token: string
  scope: string
  token_type: string
  deptId: string
  mobile: string
  userId: number
  username: string
}

