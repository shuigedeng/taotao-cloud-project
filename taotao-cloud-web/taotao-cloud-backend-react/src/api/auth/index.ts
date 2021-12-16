import { getFetch, postFetch } from '/@/http'
import { Result } from '/@/api/model/baseModel'
import { GetCaptchaParam, LoginParam, LoginVO } from '/@/api/auth/model'

const auth = {
  getCaptcha(data: GetCaptchaParam): Promise<Result<string>> {
    return getFetch<Result<string>, GetCaptchaParam>('/code', data)
  },
  login(params: LoginParam): Promise<Result<LoginVO>> {
    return postFetch<Result<LoginVO>, LoginParam>('/auth/oauth/token', params)
  },
  logout(): Promise<Result<boolean>> {
    return postFetch<Result<boolean>, any>('/auth/oauth/logout')
  },
  getSmsCode(params = {}) {
    return getFetch('/auth/sms/code', params)
  },
  loginByMobile(params = {}) {
    return getFetch('/auth/oauth/token/mobile', params)
  },
  loginBySocial(params = {}) {
    return getFetch('/auth/oauth/token/third', params)
  },
  getSocialUrl(params = {}) {
    return getFetch('/auth/social/url', params)
  }
}

export default auth
