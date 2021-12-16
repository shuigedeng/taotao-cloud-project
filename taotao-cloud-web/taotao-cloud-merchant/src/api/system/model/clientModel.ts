// 引入基础包
import { BasicPageParams, BasicFetchResult } from '/@/api/model/baseModel';

// 定义查询参数
export type ClientParams = BasicPageParams & {
  id?: string;
  clientId?: string;
};

// 定义客户端对象
export interface ClientListItem {
  id: string;
  clientId: string;
  clientSecret: string;
  resourceIds: string;
  scope: string;
  authorizedGrantTypes: string;
  webServerRedirectUri: string;
  authorities: string;
  accessTokenValidity: string;
  refreshTokenValidity: string;
  additionalInformation: string;
  autoapprove: string;
  status: string;
  createTime: string;
}

// 根据客户端对象生成响应模型
export type ClientListGetResultModel = BasicFetchResult<ClientListItem>;
