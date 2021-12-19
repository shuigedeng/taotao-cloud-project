// 引入基础包
import { BasicPageParams, BasicFetchResult } from '/@/api/model/baseModel';

// 定义查询参数
export type MenuVO = BasicPageParams & {
  name?: string;
  path?: string;
};

// 定义菜单对象
export interface Menu {
  id: string;
  name: string;
  permission: string;
  path: string;
  component: string;
  parentId: string;
  icon: string;
  sort: string;
  keepAlive: string;
  type: string;
  hidden: string;
  target: string;
  status: number;
  menu: [];
}

// 根据菜单对象生成响应模型
export type MenuDTO = BasicFetchResult<Menu>;
