import { BasicColumn, FormSchema } from '/@/components/Table';
import { h } from 'vue';
import { Tag } from 'ant-design-vue';

export const columns: BasicColumn[] = [
  {
    title: '原文件名',
    dataIndex: 'name',
    width: 100,
  },
  {
    title: '存储文件名',
    dataIndex: 'fileName',
    width: 100,
  },
  {
    title: '图片(点击预览)',
    dataIndex: 'url',
    width: 100,
    slots: { customRender: 'img' },
  },
  {
    title: '文件大小',
    dataIndex: 'size',
    width: 130,
    slots: { customRender: 'fileSize' },
  },
  {
    title: '文件类型',
    dataIndex: 'status',
    width: 80,
    customRender: ({ record }) => {
      const type = record.type;
      const enable = ~~type === 1;
      const color = enable ? 'green' : 'red';
      const text = enable ? '图片' : '其他';
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
