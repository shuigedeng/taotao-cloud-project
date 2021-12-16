// 引入基础包
import { BasicPageParams, BasicFetchResult } from '/@/api/model/baseModel';

// 定义查询参数
export type RouteVO = BasicPageParams & {
  keyword?: string;
  startDate?: string;
  endDate?: string;
};

// 定义路由对象
export interface Route {
  id: string;
  name: string;
  path: string;
  url: string;
  serviceId: string;
  status: string;
  tenantId: string;
  createTime: string;
}

// 根据字典对象生成响应模型
export type RouteDTO = BasicFetchResult<RouteVO>;
