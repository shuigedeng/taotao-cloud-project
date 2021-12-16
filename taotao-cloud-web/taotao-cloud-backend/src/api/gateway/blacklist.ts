import { BlacklistVO, BlacklistDTO, Blacklist } from './model/blacklistModel';
import { defHttp } from '/@/utils/http/axios';

enum Api {
  Page = '/mate-system/blacklist/page',
  Set = '/mate-system/blacklist/set',
  Del = '/mate-system/blacklist/del',
  Tree = '/mate-system/blacklist/list-item',
}

// 分页查询
export const page = (params: BlacklistVO) => defHttp.get<BlacklistDTO>({ url: Api.Page, params });

// 树
export const tree = () => defHttp.get({ url: Api.Tree });

// 保存
export const set = (params: Blacklist) => defHttp.post<Blacklist>({ url: Api.Set, params });

// 删除
export const del = (params: { ids: String }) =>
  defHttp.post<boolean>({ url: Api.Del + `?ids=${params.ids}` });
