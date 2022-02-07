// 引入基础包
import { BasicPageParams, BasicFetchResult } from '/@/api/model/baseModel';

// 定义查询参数
export type RoleVO = BasicPageParams & {
  id?: string;
  roleName?: string;
};

// 定义角色对象
export interface Role {
  id: string;
  roleName: string;
  roleCode: string;
  description: string;
  sort: string;
  status: string;
}

// 根据角色对象生成响应模型
export type RoleDTO = BasicFetchResult<Role>;
