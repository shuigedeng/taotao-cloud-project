import { AttachmentVO, AttachmentDTO, Attachment } from './model/attachmentModel';
import { defHttp } from '/@/utils/http/axios';

enum Api {
  Page = '/mate-component/attachment/page',
  Set = '/mate-component/attachment/set',
  Del = '/mate-component/attachment/del',
}

// 分页查询
export const page = (params: AttachmentVO) => defHttp.get<AttachmentDTO>({ url: Api.Page, params });

// 保存
export const set = (params: Attachment) => defHttp.post<Attachment>({ url: Api.Set, params });

// 删除
export const del = (params: { ids: String }) =>
  defHttp.post<boolean>({ url: Api.Del + `?ids=${params.ids}` });
