import {BasicPageParams} from "../model/baseModel";

export type LoginParam = {
  id: number,
  name: string,
  password: string
}

export type LoginVO = {
  id: number,
  name: string,
  password: string
}


export interface QueryUserParam extends BasicPageParams {
  id: number,
  name: string
}
