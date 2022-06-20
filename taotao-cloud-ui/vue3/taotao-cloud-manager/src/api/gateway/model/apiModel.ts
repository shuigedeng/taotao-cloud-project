// 引入基础包
import { BasicPageParams, BasicFetchResult } from '/@/api/model/baseModel';

// 定义查询参数
export type ApiVO = BasicPageParams & {
  keyword?: string;
  startDate?: string;
  endDate?: string;
};

// 定义系统接口对象
export interface Api {
  id: string;
  code: string;
  name: string;
  notes: string;
  method: string;
  className: string;
  methodName: string;
  path: string;
  contentType: string;
  serviceId: string;
  status: string;
  auth: string;
  createTime: string;
}

// 根据字典对象生成响应模型
export type ApiDTO = BasicFetchResult<ApiVO>;
