// 引入基础包
import { BasicPageParams, BasicFetchResult } from '/@/api/model/baseModel';

// 定义查询参数
export type DepartVO = BasicPageParams & {
  id?: string;
  name?: string;
};

// 定义部门对象
export interface Depart {
  id: string;
  name: string;
  sort: string;
  parentId: string;
}

// 根据部门对象生成响应模型
export type DepartDTO = BasicFetchResult<Depart>;
