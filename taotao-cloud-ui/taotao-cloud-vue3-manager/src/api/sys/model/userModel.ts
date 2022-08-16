/**
 * @description: Login interface parameters
 */
export interface LoginParams {
  username: string;
  password: string;
  key: string;
  code: string;
}

export interface RoleInfo {
  roleName: string;
  value: string;
}

/**
 * @description: Login interface return value
 */
export interface LoginResultModel {
  userId: string | number;
  token: string;
  role: RoleInfo;
  accessToken: string;
  avatar: string;
  jti: string;
  refreshToken: string;
  roleId: string | number;
  tenantId: string;
  userName: string;
}

/**
 * @description: Get user information return value
 */
export interface GetUserInfoModel {
  roles: RoleInfo[];
  // 用户id
  userId: string | number;
  // 用户名
  username: string;
  // 真实名字
  realName: string;
  // 头像
  avatar: string;
  // 介绍
  desc?: string;
  // 角色Id
  roleId: string | number;
}

export interface GetCaptchaModel {
  // 验证码地址
  codeUrl: string;
  // 惟一key
  key: string;
}
