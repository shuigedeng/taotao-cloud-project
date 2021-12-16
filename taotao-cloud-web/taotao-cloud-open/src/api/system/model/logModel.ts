// 引入基础包
import { BasicPageParams, BasicFetchResult } from '/@/api/model/baseModel';

// 定义查询参数
export type LogParams = BasicPageParams & {
  title?: string;
  traceId?: string;
};

// 定义日志对象
export interface LogListItem {
  type: string;
  traceId: string;
  title: string;
  operation: string;
  method: string;
  url: string;
  params: string;
  ip: string;
  executeTime: string;
  location: string;
  createTime: string;
  exception: string;
}

// 根据日志对象生成响应模型
export type LogListGetResultModel = BasicFetchResult<LogListItem>;
