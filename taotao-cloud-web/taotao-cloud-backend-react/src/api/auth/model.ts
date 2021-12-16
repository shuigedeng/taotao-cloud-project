export type GetCaptchaParam = {
  t: number
}

export type LoginParam = {
  grant_type: string
  client_id: string
  client_secret: string
  username?: string
  email?: string
  password: string
  scope: string
  t: number
  code: string
}

export type LoginVO = {
  access_token: string
  token_type: string
  refresh_token: string
  expires_in: number
  scope: string
  deptId: number
  userId: number
  phone: string
  username: string
  roles: string[]
  permissions: string[]
}
