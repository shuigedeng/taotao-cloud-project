// 引入基础包
import { BasicPageParams, BasicFetchResult } from '/@/api/model/baseModel';

// 定义查询参数
export type UserVO = BasicPageParams & {
  name?: string;
  path?: string;
};

// 定义用户对象
export interface User {
  id: string;
  account: string;
  password?: string;
  name: string;
  realName: string;
  avatar: string;
  email: string;
  telephone: string;
  birthday: string;
  sex: number;
  roleId: string | number;
  departId: string | number;
  status: number;
  departName: string;
  roleName: string;
}

// 根据用户对象生成响应模型
export type UserDTO = BasicFetchResult<User>;
