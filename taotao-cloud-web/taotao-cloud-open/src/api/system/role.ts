import { Role, RoleVO, RoleDTO } from './model/roleModel';
import { defHttp } from '/@/utils/http/axios';

enum Api {
  Page = '/mate-system/role/page',
  Set = '/mate-system/role/set',
  Del = '/mate-system/role/delete',
  SetStatus = '/mate-system/role/set-status',
  AllList = '/mate-system/role/all-list',
}

// 菜单树
export const rolePage = (params?: RoleVO) => defHttp.get<RoleDTO>({ url: Api.Page, params });

// 保存
export const roleSet = (params: Role) => defHttp.post<Role>({ url: Api.Set, params });

// 删除
export const roleDel = (params: { ids: String }) =>
  defHttp.post<boolean>({ url: Api.Del + `?ids=${params.ids}` });

// 设置状态
export const roleSetStatus = (id: number, status: string) =>
  defHttp.post({ url: Api.SetStatus, params: { id, status } });

// 查询所有角色列表
export const roleAllList = (params?: RoleVO) => defHttp.get<RoleDTO>({ url: Api.AllList, params });
