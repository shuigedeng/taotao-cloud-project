import { User, UserVO, UserDTO } from './model/userModel';
import { defHttp } from '/@/utils/http/axios';

enum Api {
  Page = '/mate-system/user/page',
  Set = '/mate-system/user/set',
  Del = '/mate-system/user/del',
  SetPassword = '/mate-system/user/set-password',
}

// 菜单树
export const page = (params?: UserVO) => defHttp.get<UserDTO>({ url: Api.Page, params });

// 保存
export const set = (params: User) => defHttp.post<User>({ url: Api.Set, params });

// 删除
export const del = (params: { ids: String }) =>
  defHttp.post<boolean>({ url: Api.Del + `?ids=${params.ids}` });

// 设置密码
export const userSetPassword = (params?: User) =>
  defHttp.post<boolean>({ url: Api.SetPassword, params });
