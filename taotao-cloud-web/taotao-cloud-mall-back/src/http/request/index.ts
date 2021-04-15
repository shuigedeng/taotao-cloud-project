import Taro, {request as TaroRequest} from '@tarojs/taro'
import getRequestUrl from './requestUrl'
import interceptors from './interceptors'
import {ContentTypeEnum, RequestEnum} from "../../enums/httpEnum";

interceptors.forEach(interceptorItem => Taro.addInterceptor(interceptorItem))

interface options<P> {
  url: string,
  data: P,
  contentType?: string,
  header?: any,
  method: RequestEnum
}

const create = <T, P>({url, data, contentType, method}: options<P>): Promise<T> => {
  const option: any = {
    url: getRequestUrl(url),
    data: data,
    method: method,
    header: {
      'content-type': contentType && ContentTypeEnum.JSON,
      // 'Authorization': Taro.getStorageSync('Authorization')
      'Authorization': "9999999999999"
    }
  }
  return Taro.request<T, P>(option).then((res: TaroRequest.SuccessCallbackResult<T>) => {
    return Promise.resolve(res?.data);
  }).catch((err: Error) => {
    console.log(err.message)
    return Promise.reject();
  })
}

const request = {
  get: <T, P>(url: string, data: P, contentType?: string): Promise<T> => {
    return create<T, P>({url, data, contentType, method: RequestEnum.GET})
  },
  post: <T, P>(url: string, data: P, contentType?: string): Promise<T> => {
    return create<T, P>({url, data, contentType, method: RequestEnum.POST})
  },
  put: <T, P>(url: string, data: P): Promise<T> => {
    return create<T, P>({url, data, method: RequestEnum.PUT})
  },
  delete: <T, P>(url: string, data: P): Promise<T> => {
    return create<T, P>({url, data, method: RequestEnum.DELETE})
  },
}

export default request;
