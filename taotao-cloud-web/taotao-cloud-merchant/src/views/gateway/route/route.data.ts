import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
import { h } from 'vue';
import { Tag } from 'ant-design-vue';

export const columns: BasicColumn[] = [
  {
    title: '名称',
    dataIndex: 'name',
    width: 100,
  },
  {
    title: '服务ID',
    dataIndex: 'serviceId',
    width: 100,
  },
  {
    title: '服务路径',
    dataIndex: 'path',
    width: 130,
  },
  {
    title: 'URI',
    dataIndex: 'url',
    width: 90,
  },
  {
    title: '状态',
    dataIndex: 'status',
    width: 80,
    customRender: ({ record }) => {
      const status = record.status;
      const enable = ~~status === 0;
      const color = enable ? 'green' : 'red';
      const text = enable ? '启用' : '停用';
      return h(Tag, { color: color }, () => text);
    },
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 100,
  },
];

export const searchFormSchema: FormSchema[] = [
  {
    field: 'keyword',
    label: '关键字',
    component: 'Input',
    componentProps: {
      placeholder: '请输入名称/服务ID',
    },
    colProps: { span: 8 },
  },
  {
    field: 'startDate',
    label: '起始时间',
    component: 'DatePicker',
    colProps: { span: 8 },
  },
  {
    field: 'endDate',
    label: '截止时间',
    component: 'DatePicker',
    colProps: { span: 8 },
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
    field: 'name',
    label: '名称',
    component: 'Input',
  },
  {
    field: 'serviceId',
    label: '服务ID',
    component: 'Input',
  },
  {
    field: 'path',
    label: '服务路径',
    component: 'Input',
  },
  {
    field: 'url',
    label: 'URI',
    component: 'Input',
  },
  {
    field: 'status',
    label: '状态',
    component: 'RadioButtonGroup',
    defaultValue: '0',
    componentProps: {
      options: [
        { label: '启用', value: '0' },
        { label: '禁用', value: '1' },
      ],
    },
  },
];
