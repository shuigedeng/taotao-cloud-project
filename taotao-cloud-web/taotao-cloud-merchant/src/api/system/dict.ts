import { DictVO, DictDTO, Dict } from './model/dictModel';
import { defHttp } from '/@/utils/http/axios';

enum Api {
  Page = '/mate-system/dict/page',
  Set = '/mate-system/dict/set',
  Del = '/mate-system/dict/del',
  SubPage = '/mate-system/dict/list-value',
}

// 分页查询
export const page = (params: DictVO) => defHttp.get<DictDTO>({ url: Api.Page, params });

// 字典项分页查询
export const subPage = (params: DictVO) => defHttp.get<DictDTO>({ url: Api.SubPage, params });

// 保存
export const set = (params: Dict) => defHttp.post<Dict>({ url: Api.Set, params });

// 删除
export const del = (params: { ids: String }) =>
  defHttp.post<boolean>({ url: Api.Del + `?ids=${params.ids}` });
