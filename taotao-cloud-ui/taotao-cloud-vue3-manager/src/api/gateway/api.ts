import { ApiVO, ApiDTO, Api as SysApi } from './model/apiModel';
import { defHttp } from '/@/utils/http/axios';

enum Api {
  Page = '/mate-system/api/page',
  Set = '/mate-system/api/set',
  Del = '/mate-system/api/del',
  Sync = '/mate-system/api/sync',
}

// 分页查询
export const page = (params: ApiVO) => defHttp.get<ApiDTO>({ url: Api.Page, params });

// 保存
export const set = (params: SysApi) => defHttp.post<SysApi>({ url: Api.Set, params });

// 同步
export const sync = () => defHttp.post({ url: Api.Sync });

// 删除
export const del = (params: { ids: String }) =>
  defHttp.post<boolean>({ url: Api.Del + `?ids=${params.ids}` });
