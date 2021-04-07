import { message } from 'antd'
import { getLoginUserToken } from '/@/utils/lsUtil'
import { ContentTypeEnum, HttpStatusEnum, RequestEnum } from '/@/enums/httpEnum'
import { fetchInterceptor } from '/@/http/fetch/interceptor'

class BusinessError extends Error {
  constructor(message: string) {
    super(message)
    this.name = 'BusinessError'
  }
}

class HttpError extends Error {
  constructor(message: string) {
    super(message)
    this.name = 'HttpError'
  }
}

const handleUrl = (url: string) => (params: any): string => {
  if (params) {
    const paramsArray: string[] = []
    Object.keys(params).forEach(key =>
      paramsArray.push(key + '=' + encodeURIComponent(params[key]))
    )
    if (url.search(/\?/) === -1) {
      url += '?' + paramsArray.join('&')
    } else {
      url += '&' + paramsArray.join('&')
    }
  }
  return url
}

const handleHeaders = (contentType: string = ContentTypeEnum.JSON): Headers => {
  const headers: Headers = new Headers()
  const token = getLoginUserToken()
  if (token) {
    headers.append('Authorization', 'bearer ' + token)
  }
  headers.append('CLOUD_HEADER', 'CLOUD_HEADER_VALUE')
  // headers.append("Access-Control-Allow-Origin", "*");
  // headers.append("Access-Control-Allow-Headers", "*");
  // headers.append("Access-Control-Allow-Methods", "GET, POST, PATCH, PUT, DELETE, OPTIONS");
  // headers.append("Access-Control-Allow-Credentials", "false");
  headers.append('BasicAuthorization', 'Basic d2ViQXBwOndlYkFwcA==')
  headers.set('Content-Type', contentType)
  return headers
}

const handleResponse = (res: Response): any => {
  if (res.status === HttpStatusEnum.NOT_FOUND) {
    throw new HttpError('请求资源不存在!')
  } else if (res.status === HttpStatusEnum.BAD_GATEWAY) {
    throw new HttpError('网关错误!')
  } else if (res.status === HttpStatusEnum.FORBIDDEN) {
    throw new HttpError('没有权限访问接口!')
  } else if (res.status === HttpStatusEnum.AUTHENTICATE) {
    throw new HttpError('用户未认证!')
  } else if (res.status === HttpStatusEnum.SUCCESS) {
    return res.json()
  } else {
    throw new HttpError('服务器错误,请稍后重试!')
  }
}

const handleResponseData = (response: any): any => {
  console.log(response)
  if (response.code === 200) {
    return response
  } else {
    throw new BusinessError(response.message)
  }
}

const handleResponseError = (error: Error): any => {
  message.destroy()
  if (error instanceof TypeError) {
    message.error({
      content: '当前网络不可用, 请检查网络设置! ',
      className: 'custom-class',
      style: {
        borderRadius: '7px',
        marginTop: '50%'
      },
      duration: 5
    })
  } else {
    message.error(error.message, 5)
  }
}

const get = <R, P = {}>(
  url: string,
  params?: P,
  options?: RequestInit
): Promise<R> => {
  options = {
    method: RequestEnum.GET,
    mode: 'cors',
    headers: handleHeaders()
  }
  return fetch(handleUrl(url)(params), options)
    .then<R>((response: Response) => {
      return handleResponse(response)
    })
    .then<any>((data: any) => {
      return handleResponseData(data)
    })
    .catch<any>((error: Error) => {
      handleResponseError(error)
    })
}

const post = <R, P = {}>(url: string, params?: P): Promise<R> => {
  const options: RequestInit = {
    method: RequestEnum.POST,
    mode: 'cors',
    headers: handleHeaders(),
    body: JSON.stringify(params)
  }
  return fetch(url, options)
    .then<R>((response: Response) => {
      return handleResponse(response)
    })
    .then<any>((data: any) => {
      return handleResponseData(data)
    })
    .catch<any>((error: Error) => {
      handleResponseError(error)
    })
}

const put = <R, P = {}>(url: string, params?: P): Promise<R> => {
  return fetch(url, {
    method: RequestEnum.PUT,
    mode: 'cors',
    body: JSON.stringify(params),
    headers: handleHeaders()
  })
    .then<R>((response: Response) => {
      return handleResponse(response)
    })
    .then<any>((data: any) => {
      return handleResponseData(data)
    })
    .catch<any>((error: Error) => {
      handleResponseError(error)
    })
}

const del = <R, P = {}>(url: string, params?: P): Promise<R> => {
  return fetch(url, {
    method: RequestEnum.DELETE,
    mode: 'cors',
    body: JSON.stringify(params),
    headers: handleHeaders()
  })
    .then<R>((response: Response) => {
      return handleResponse(response)
    })
    .then<any>((data: any) => {
      return handleResponseData(data)
    })
    .catch<any>((error: Error) => {
      handleResponseError(error)
    })
}

fetchInterceptor.interceptors.push(
  {
    request: (input: string, init: RequestInit) => {
      // 请求拦截器
      return { input, init }
    }
  },
  {
    response: (res: Response) => {
      // 返回拦截器
      return res
    }
  }
)

export { get, post, put, del }
