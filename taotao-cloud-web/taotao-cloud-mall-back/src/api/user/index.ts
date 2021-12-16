import request from "../../http/request";
import {LoginParam, LoginVO, QueryUserParam} from "./model";
import {Page, Result} from "../model/baseModel";

export default {
  login(data: LoginParam): Promise<Result<LoginVO>> {
    return request.post<Result<LoginVO>, LoginParam>("/auth/oauth/token/user", data);
  },
  queryUser(data: QueryUserParam): Promise<Page<LoginVO>> {
    return request.get<Page<LoginVO>, QueryUserParam>("/auth/oauth/token/user", data);
  },
  getLoginUserInfo(): Promise<Result<any>> {
    return request.get("/uc/current", {});
  },
  bindPhone(data: any): Promise<Result<any>> {
    return request.get("/uc/bindPhone", data);
  },
  logOut(): Promise<Result<any>> {
    return request.get("/uc/logout", {});
  },
}
