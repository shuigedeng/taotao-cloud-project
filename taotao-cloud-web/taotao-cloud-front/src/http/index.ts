import {del, get, post, put} from "./fetch";
import getRequestUrl from "./requestUrl";

// ********************fetch*************************
const getFetch = <R, P>(url: string, params: P): Promise<R> => {
  return get<R, P>(getRequestUrl(url), params);
}

const postFetch = <R, P>(url: string, params: P): Promise<R> => {
  return post<R, P>(getRequestUrl(url), params)
}

const putFetch = <R, P>(url: string, params: P): Promise<R> => {
  return put<R, P>(getRequestUrl(url), params)
}

const delFetch = <R, P>(url: string, params: P): Promise<R> => {
  return del<R, P>(getRequestUrl(url), params)
}

export {getFetch, postFetch, putFetch, delFetch}
