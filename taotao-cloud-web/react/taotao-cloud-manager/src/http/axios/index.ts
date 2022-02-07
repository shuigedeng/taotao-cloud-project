// axios配置  可自行根据项目进行更改，只需更改该文件即可，其他文件可以不动
// The axios configuration can be changed according to the project, just change the file, other files can be left unchanged
import { message as antdMessage } from 'antd'
import { AxiosResponse } from 'axios'
import { CreateAxiosOptions, ErrorMessageMode, RequestOptions } from './types'

import { VAxios } from './Axios'
import { AxiosTransform } from './axiosTransform'
import {
  ContentTypeEnum,
  HttpStatusEnum,
  RequestEnum,
  ResultEnum
} from '/@/enums/httpEnum'
import { isString } from '/@/utils/is'
import { Result } from '/@/api/model/baseModel'
import { formatRequestDate } from '/@/utils/dateUtil'
import { deepMerge, setObjToUrlParams } from '/@/utils'

const prefix = ''

/**
 * @description: 数据处理，方便区分多种处理方式
 */
const transform: AxiosTransform = {
  /**
   * @description: 处理请求数据
   */
  transformRequestData: (
    res: AxiosResponse<Result<any>>,
    options: RequestOptions
  ) => {
    const { isTransformRequestResult } = options
    // 不进行任何处理，直接返回
    // 用于页面代码可能需要直接获取code，data，message这些信息时开启
    if (!isTransformRequestResult) {
      return res.data
    }

    const { data } = res
    if (!data) {
      // return '[HTTP] Request has no return value';
      return
    }
    //  这里 code，result，message为 后台统一的字段，需要在 types.ts内修改为项目自己的接口返回格式
    const hasSuccess =
      data && Reflect.has(data, 'code') && data.code === ResultEnum.SUCCESS
    if (hasSuccess) {
      return Promise.resolve(data)
    } else {
      if (data.message) {
        throw new Error(data.message)
      } else {
        throw new Error('网络错误,请稍后重试')
      }
    }
  },
  // 请求之前处理config
  beforeRequestHook: (config, options) => {
    const { apiUrl, joinPrefix, joinParamsToUrl, formatDate } = options

    if (joinPrefix) {
      config.url = `${prefix}${config.url}`
    }

    if (apiUrl && isString(apiUrl)) {
      config.url = `${apiUrl}${config.url}`
    }
    if (config.method?.toUpperCase() === RequestEnum.GET) {
      const now = new Date().getTime()
      if (!isString(config.params)) {
        config.data = {
          // 给 get 请求加上时间戳参数，避免从缓存中拿数据。
          params: Object.assign(config.params || {}, {
            _t: now
          })
        }
      } else {
        // 兼容restful风格
        config.url = config.url + config.params + `?_t=${now}`
        config.params = undefined
      }
    } else {
      if (!isString(config.params)) {
        formatDate && formatRequestDate(config.params)
        config.data = config.params
        config.params = undefined
        if (joinParamsToUrl) {
          config.url = setObjToUrlParams(config.url as string, config.data)
        }
      } else {
        // 兼容restful风格
        config.url = config.url + config.params
        config.params = undefined
      }
    }
    return config
  },

  /**
   * @description: 请求拦截器处理
   */
  requestInterceptors: config => {
    const token = 'l23sldfkslfasldfj8wrljslfi3232'
    if (token) {
      config.headers.Authorization = token
    }
    return config
  },

  /**
   * @description: 响应错误处理
   */
  responseInterceptorsCatch: (error: any) => {
    const { code, message } = error || {}
    const err: string = error?.toString()

    try {
      if (code === 'ECONNABORTED' && message.indexOf('timeout') !== -1) {
        antdMessage.error({
          content: '请求超时,请检查网络',
          className: 'custom-class',
          style: {
            borderRadius: '7px',
            marginTop: '50%'
          },
          duration: 5
        })
      }
      if (err?.includes('Network Error')) {
        antdMessage.error({
          content: '请求错误,请检查网络',
          className: 'custom-class',
          style: {
            borderRadius: '7px',
            marginTop: '50%'
          },
          duration: 5
        })
      }
    } catch (error) {
      throw new Error(error)
    }

    const status = error?.response?.status
    if (status === HttpStatusEnum.NOT_FOUND) {
      antdMessage.error('请求资源不存在')
    } else if (status === HttpStatusEnum.BAD_GATEWAY) {
      antdMessage.error('网关错误')
    } else if (status === HttpStatusEnum.FORBIDDEN) {
      antdMessage.error('没有权限访问接口')
    } else if (status === HttpStatusEnum.AUTHENTICATE) {
      antdMessage.error('用户未认证')
    }
    return Promise.reject(error)
  }
}

function createAxios(opt?: Partial<CreateAxiosOptions>) {
  return new VAxios(
    deepMerge(
      {
        timeout: 10 * 1000,
        // 基础接口地址
        // baseURL: globSetting.apiUrl,
        // 接口可能会有通用的地址部分，可以统一抽取出来
        prefixUrl: prefix,
        headers: { 'Content-Type': ContentTypeEnum.JSON },
        // 数据处理方式
        transform,
        // 配置项，下面的选项都可以在独立的接口请求中覆盖
        requestOptions: {
          // 默认将prefix 添加到url
          joinPrefix: true,
          // 需要对返回数据进行处理
          isTransformRequestResult: true,
          // post请求的时候添加参数到url
          joinParamsToUrl: false,
          // 格式化提交参数时间
          formatDate: false,
          // 消息提示类型
          errorMessageMode: 'message',
          // 接口地址
          apiUrl: opt?.url
        }
      },
      opt || {}
    )
  )
}

export const defHttp = createAxios()

export const get = <R, P = {}>(url: string, params: P): Promise<R> => {
  return defHttp.request<R>({
    url: url,
    method: 'GET',
    params
  })
}

export const post = <R, P = {}>(
  url: string,
  params: P,
  mode: ErrorMessageMode = 'modal'
): Promise<R> => {
  return defHttp.request<R>(
    {
      url: url,
      method: 'POST',
      params
    },
    {
      errorMessageMode: mode
    }
  )
}

export const put = <R, P = {}>(
  url: string,
  params: P,
  mode: ErrorMessageMode = 'modal'
): Promise<R> => {
  return defHttp.request<R>(
    {
      url: url,
      method: 'PUT',
      params
    },
    {
      errorMessageMode: mode
    }
  )
}

export const del = <R, P = {}>(
  url: string,
  params: P,
  mode: ErrorMessageMode = 'modal'
): Promise<R> => {
  return defHttp.request<R>(
    {
      url: url,
      method: 'DELETE',
      params
    },
    {
      errorMessageMode: mode
    }
  )
}
