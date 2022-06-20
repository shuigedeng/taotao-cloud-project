import { DepartVO, DepartDTO, Depart } from './model/departModel';
import { defHttp } from '/@/utils/http/axios';

enum Api {
  List = '/mate-system/depart/list',
  Set = '/mate-system/depart/set',
  Del = '/mate-system/depart/del',
}

// 菜单树
export const departList = (params?: DepartVO) => defHttp.get({ url: Api.List, params });

// 保存
export const departSet = (params: Depart) => defHttp.post<Depart>({ url: Api.Set, params });

// 删除
export const departDel = (params: { ids: String }) =>
  defHttp.post<boolean>({ url: Api.Del + `?ids=${params.ids}` });
