import { del, get, post, put } from './fetch'
import { del as Adel, get as Aget, post as Apost, put as Aput } from './axios'
import getRequestUrl from './requestUrl'

// ********************fetch*************************
const getFetch = <R, P>(url: string, params?: P): Promise<R> => {
  return get<R, P>(getRequestUrl(url), params)
}

const postFetch = <R, P>(url: string, params?: P): Promise<R> => {
  return post<R, P>(getRequestUrl(url), params)
}

const putFetch = <R, P>(url: string, params?: P): Promise<R> => {
  return put<R, P>(getRequestUrl(url), params)
}

const delFetch = <R, P>(url: string, params?: P): Promise<R> => {
  return del<R, P>(getRequestUrl(url), params)
}

export { getFetch, postFetch, putFetch, delFetch }

// ********************axios*************************
export const getAxios = <R, P>(url: string, params: P): Promise<R> => {
  return Aget<R, P>(getRequestUrl(url), params)
}
export const postAxios = <R, P>(url: string, params: P): Promise<R> => {
  return Apost<R, P>(getRequestUrl(url), params)
}
export const putAxios = <R, P>(url: string, params: P): Promise<R> => {
  return Aput<R, P>(getRequestUrl(url), params)
}
export const delAxios = <R, P>(url: string, params: P): Promise<R> => {
  return Adel<R, P>(getRequestUrl(url), params)
}
