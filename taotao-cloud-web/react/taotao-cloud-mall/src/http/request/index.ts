import Taro, {request as TaroRequest} from '@tarojs/taro'
import getRequestUrl from './requestUrl'
import interceptors from './interceptors'
import {ContentTypeEnum, HTTP_STATUS, RequestEnum} from "@/enums/httpEnum";
import {Result} from "@/api/model/baseModel";
import {error} from "@/utils/tool";

interface options<P> {
  url: string,
  data: P,
  contentType?: string,
  header?: any,
  method: RequestEnum,
  mode?: string,
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
    },
    mode: 'no-cors',
  }
  interceptors.forEach(interceptorItem => Taro.addInterceptor(interceptorItem))
  // return Taro.request<Result<T>, P>(option).then((res: TaroRequest.SuccessCallbackResult<Result<T>>) => {
  //   if (res.statusCode === HTTP_STATUS.SUCCESS) {
  //     let code = res.data.code;
  //     if (code == HTTP_STATUS.SUCCESS) {
  //       return Promise.resolve(res.data);
  //     }
  //     error(res.data.message)
  //   }
  //   error('服务器错误,请稍后重试')
  // }).catch((err: Error) => {
  //   console.log('-----------')
  //   error('服务器错误,请稍后重试')
  //   return Promise.resolve(null);
  // })
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
