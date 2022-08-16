import { ClientParams, ClientListGetResultModel, ClientListItem } from './model/clientModel';
import { defHttp } from '/@/utils/http/axios';

enum Api {
  Page = '/mate-system/client/page',
  Set = '/mate-system/client/set',
  Del = '/mate-system/client/del',
}

// 分页查询
export const page = (params: ClientParams) =>
  defHttp.get<ClientListGetResultModel>({ url: Api.Page, params });

// 保存
export const set = (params: ClientListItem) =>
  defHttp.post<ClientListItem>({ url: Api.Set, params });

// 删除
export const del = (params: { ids: String }) =>
  defHttp.post<boolean>({ url: Api.Del + `?ids=${params.ids}` });
