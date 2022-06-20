// 引入基础包
import { BasicPageParams, BasicFetchResult } from '/@/api/model/baseModel';

// 定义查询参数
export type AttachmentVO = BasicPageParams & {
  keyword?: string;
  startDate?: string;
  endDate?: string;
};

// 定义路由对象
export interface Attachment {
  id: string;
  storageId: string;
  attachmentGroupId: string;
  name: string;
  size: string;
  url: string;
  fileName: string;
  thumbUrl: string;
  type: number;
  createTime: string;
}

// 根据字典对象生成响应模型
export type AttachmentDTO = BasicFetchResult<AttachmentVO>;
