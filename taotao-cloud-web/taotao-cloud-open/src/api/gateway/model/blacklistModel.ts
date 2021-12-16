// 引入基础包
import { BasicPageParams, BasicFetchResult } from '/@/api/model/baseModel';

// 定义查询参数
export type BlacklistVO = BasicPageParams & {
  keyword?: string;
  startDate?: string;
  endDate?: string;
};

// 定义黑名单对象
export interface Blacklist {
  id: string;
  ip: string;
  requestUri: string;
  requestMethod: string;
  startTime: string;
  endTime: string;
  status: string;
  createTime: string;
}

// 根据字典对象生成响应模型
export type BlacklistDTO = BasicFetchResult<BlacklistVO>;
