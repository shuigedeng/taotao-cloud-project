import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
import { h } from 'vue';
import { Tag } from 'ant-design-vue';
import moment from 'moment';

export const columns: BasicColumn[] = [
  {
    title: 'IP地址',
    dataIndex: 'ip',
    width: 100,
  },
  {
    title: '请求地址',
    dataIndex: 'requestUri',
    width: 130,
  },
  {
    title: '请求方法',
    dataIndex: 'requestMethod',
    width: 80,
  },
  {
    title: '开始时间',
    dataIndex: 'startTime',
    width: 90,
  },
  {
    title: '结束时间',
    dataIndex: 'endTime',
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
    field: 'ip',
    label: 'IP地址',
    component: 'Input',
  },
  {
    field: 'requestUri',
    label: '请求地址',
    component: 'Input',
  },
  {
    field: 'requestMethod',
    label: '请求方法',
    component: 'Input',
  },
  {
    field: 'startTime',
    label: '开始时间',
    component: 'TimePicker',
    defaultValue: moment('09:00:00', 'HH:mm:ss'),
    componentProps: {
      valueFormat: 'HH:mm:ss',
    },
  },
  {
    field: 'endTime',
    label: '结束时间',
    component: 'TimePicker',
    defaultValue: moment('21:00:00', 'HH:mm:ss'),
    componentProps: {
      valueFormat: 'HH:mm:ss',
    },
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
