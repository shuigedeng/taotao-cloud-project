import { RouteVO, RouteDTO, Route } from './model/routeModel';
import { defHttp } from '/@/utils/http/axios';

enum Api {
  Page = '/mate-system/route/page',
  Set = '/mate-system/route/set',
  Del = '/mate-system/route/del',
  Tree = '/mate-system/route/list-item',
}

// 分页查询
export const page = (params: RouteVO) => defHttp.get<RouteDTO>({ url: Api.Page, params });

// 树
export const tree = () => defHttp.get({ url: Api.Tree });

// 保存
export const set = (params: Route) => defHttp.post<Route>({ url: Api.Set, params });

// 删除
export const del = (params: { ids: String }) =>
  defHttp.post<boolean>({ url: Api.Del + `?ids=${params.ids}` });
