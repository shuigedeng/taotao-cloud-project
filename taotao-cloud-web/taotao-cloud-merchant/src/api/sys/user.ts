import { defHttp } from '/@/utils/http/axios';
import {
  LoginParams,
  LoginResultModel,
  GetUserInfoModel,
  GetCaptchaModel,
} from './model/userModel';

import { ErrorMessageMode } from '/#/axios';
import md5 from 'js-md5';

enum Api {
  Login = '/mate-uaa/oauth/token',
  Logout = '/mate-uaa/auth/logout',
  GetUserInfo = '/mate-uaa/auth/get/user',
  GetPermCode = '/getPermCode',
  GetCaptcha = '/mate-uaa/auth/code',
}

/**
 * @description: user login api
 */
export function loginApi(params: LoginParams, mode: ErrorMessageMode = 'modal') {
  return defHttp.post<LoginResultModel>(
    {
      url: Api.Login,
      headers: {
        key: params.key,
        code: params.code,
      },
      params: {
        username: params.username,
        password: md5(params.password),
        grant_type: 'captcha',
        scope: 'all',
      },
    },
    {
      errorMessageMode: mode,
    }
  );
}

/**
 * @description: getUserInfo
 */
export function getUserInfo() {
  return defHttp.get<GetUserInfoModel>({ url: Api.GetUserInfo });
}

export function getPermCode() {
  return defHttp.get<string[]>({ url: Api.GetPermCode });
}

export function doLogout() {
  return defHttp.post({ url: Api.Logout });
}

export function getCaptcha() {
  return defHttp.get<GetCaptchaModel>({ url: Api.GetCaptcha });
}
