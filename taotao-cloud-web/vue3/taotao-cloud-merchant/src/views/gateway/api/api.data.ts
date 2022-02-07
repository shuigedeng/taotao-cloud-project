import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
import { h } from 'vue';
import { Tag } from 'ant-design-vue';

export const columns: BasicColumn[] = [
  {
    title: '编码',
    dataIndex: 'code',
    width: 100,
  },
  {
    title: '服务ID',
    dataIndex: 'serviceId',
    width: 100,
  },
  {
    title: '名称',
    dataIndex: 'name',
    width: 100,
  },
  {
    title: '请求方法',
    dataIndex: 'method',
    width: 60,
  },
  {
    title: '请求路径',
    dataIndex: 'path',
    width: 130,
  },
  {
    title: '认证',
    dataIndex: 'auth',
    width: 60,
    customRender: ({ record }) => {
      const auth = record.auth;
      const enable = ~~auth === 1;
      const color = enable ? 'green' : 'red';
      const text = enable ? '认证' : '不认证';
      return h(Tag, { color: color }, () => text);
    },
  },
  {
    title: '状态',
    dataIndex: 'status',
    width: 60,
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
      placeholder: '请输入编码/请求地址',
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
    field: 'code',
    label: '编码',
    component: 'Input',
  },
  {
    field: 'serviceId',
    label: '服务ID',
    component: 'Input',
  },
  {
    field: 'name',
    label: '名称',
    component: 'Input',
  },
  {
    field: 'method',
    label: '请求方法',
    component: 'Input',
  },
  {
    field: 'className',
    label: '类名',
    component: 'Input',
  },
  {
    field: 'methodName',
    label: '方法名',
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
