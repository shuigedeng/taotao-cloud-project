import { DatasourceVO, DatasourceDTO, Datasource } from './model/datasourceModel';
import { defHttp } from '/@/utils/http/axios';

enum Api {
  Page = '/mate-code/data-source/page',
  Set = '/mate-code/data-source/set',
  Del = '/mate-code/data-source/del',
}

// 分页查询
export const page = (params: DatasourceVO) => defHttp.get<DatasourceDTO>({ url: Api.Page, params });

// 保存
export const set = (params: Datasource) => defHttp.post<Datasource>({ url: Api.Set, params });

// 删除
export const del = (params: { ids: String }) =>
  defHttp.post<boolean>({ url: Api.Del + `?ids=${params.ids}` });
