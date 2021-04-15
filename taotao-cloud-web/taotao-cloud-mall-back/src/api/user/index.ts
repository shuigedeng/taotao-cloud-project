import request from "../../http/request";
import {LoginParam, LoginVO, QueryUserParam} from "./model";
import {PageResult, Result} from "../model/baseModel";

export default {
  login(data: LoginParam): Promise<Result<LoginVO>> {
    return request.post<Result<LoginVO>, LoginParam>("/auth/oauth/token/user", data);
  },
  queryUser(data: QueryUserParam): Promise<PageResult<LoginVO>> {
    return request.get<PageResult<LoginVO>, QueryUserParam>("/auth/oauth/token/user", data);
  }
}
