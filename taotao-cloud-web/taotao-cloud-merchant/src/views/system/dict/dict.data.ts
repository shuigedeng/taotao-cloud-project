import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';

export const columns: BasicColumn[] = [
  {
    title: '字典ID',
    dataIndex: 'id',
    width: 100,
  },
  {
    title: '字典编号',
    dataIndex: 'code',
    width: 100,
  },
  {
    title: '字典名称',
    dataIndex: 'dictValue',
    width: 130,
  },
  {
    title: '排序',
    dataIndex: 'sort',
    width: 90,
  },
];

export const searchFormSchema: FormSchema[] = [
  {
    field: 'keyword',
    label: '关键词',
    component: 'Input',
    componentProps: {
      placeholder: '请输入字典名称/编码',
    },
    colProps: { span: 16 },
  },
];

export const subSearchFormSchema: FormSchema[] = [
  {
    field: 'keyword',
    label: '关键词',
    component: 'Input',
    componentProps: {
      placeholder: '请输入字典名称/编码',
    },
    colProps: { span: 16 },
  },
];

export const formSchema: FormSchema[] = [
  {
    field: 'id',
    label: 'ID',
    component: 'Input',
    show: false,
  },
  {
    field: 'code',
    label: '字典编号',
    component: 'Input',
  },
  {
    field: 'dictValue',
    label: '字典名称',
    component: 'Input',
  },
  {
    field: 'sort',
    label: '排序',
    component: 'InputNumber',
  },
  {
    field: 'remark',
    label: '备注',
    component: 'InputTextArea',
  },
];

export const subFormSchema: FormSchema[] = [
  {
    field: 'id',
    label: 'ID',
    component: 'Input',
    show: false,
  },
  {
    field: 'parentId',
    label: '上级ID',
    component: 'Input',
    show: false,
  },
  {
    field: 'code',
    label: '字典编号',
    component: 'Input',
    componentProps: { disabled: true },
  },
  {
    field: 'dictKey',
    label: '字典值',
    component: 'Input',
  },
  {
    field: 'dictValue',
    label: '字典名称',
    component: 'Input',
  },
  {
    field: 'sort',
    label: '排序',
    component: 'InputNumber',
  },
  {
    field: 'remark',
    label: '备注',
    component: 'InputTextArea',
  },
];
