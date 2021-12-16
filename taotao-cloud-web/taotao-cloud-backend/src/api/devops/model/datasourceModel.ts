// 引入基础包
import { BasicPageParams, BasicFetchResult } from '/@/api/model/baseModel';

// 定义查询参数
export type DatasourceVO = BasicPageParams & {
  keyword?: string;
  startDate?: string;
  endDate?: string;
};

// 定义对象
export interface Datasource {
  id: string;
  name: string;
  dbType: string;
  driverClass: string;
  url: string;
  username: string;
  password: string;
  remark: string;
  status: string;
  createTime: string;
}

// 根据字典对象生成响应模型
export type DatasourceDTO = BasicFetchResult<DatasourceVO>;
