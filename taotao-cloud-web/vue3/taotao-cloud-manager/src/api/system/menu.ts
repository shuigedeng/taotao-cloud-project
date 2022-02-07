import { MenuVO, Menu } from './model/menuModel';
import { defHttp } from '/@/utils/http/axios';

enum Api {
  List = '/mate-system/menu/list',
  Set = '/mate-system/menu/set',
  Del = '/mate-system/menu/del',
  CheckChild = '/mate-system/menu/check-child',
}

// 菜单树
export const menuList = (params?: MenuVO) => defHttp.get({ url: Api.List, params });

// 保存
export const menuSet = (params: Menu) => defHttp.post<Menu>({ url: Api.Set, params });

// 删除
export const menuDel = (params: { ids: String }) =>
  defHttp.post<boolean>({ url: Api.Del + `?ids=${params.ids}` });

// 是否包含子菜单
export const menuCheckChild = (params?: { id: String }) =>
  defHttp.get({ url: Api.CheckChild, params });
