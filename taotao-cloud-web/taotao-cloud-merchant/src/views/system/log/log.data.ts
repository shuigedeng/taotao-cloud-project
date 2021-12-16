import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
import { h } from 'vue';
import { Tag } from 'ant-design-vue';

export const columns: BasicColumn[] = [
  {
    title: 'TraceId',
    dataIndex: 'traceId',
    width: 100,
  },
  {
    title: '操作',
    dataIndex: 'title',
    width: 100,
  },
  {
    title: '路径',
    dataIndex: 'url',
    width: 130,
  },
  {
    title: '方法',
    dataIndex: 'method',
    width: 60,
    customRender: ({ record }) => {
      return h(Tag, { color: 'blue' }, () => record.method);
    },
  },
  {
    title: '耗时(ms)',
    dataIndex: 'executeTime',
    width: 80,
    customRender: ({ record }) => {
      const time = record.executeTime;
      const color = time > 1000 ? 'red' : 'green';
      return h(Tag, { color: color }, () => time);
    },
  },
  {
    title: 'IP地址',
    dataIndex: 'ip',
    width: 100,
  },
  {
    title: '地区',
    dataIndex: 'location',
    width: 100,
  },
  {
    title: '类型',
    dataIndex: 'type',
    width: 120,
    customRender: ({ record }) => {
      const type = record.type;
      const enable = ~~type === 1;
      const color = enable ? 'green' : 'red';
      const text = enable ? '日志' : '异常';
      return h(Tag, { color: color }, () => text);
    },
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 180,
  },
];

export const searchFormSchema: FormSchema[] = [
  {
    field: 'keyword',
    label: '关键字',
    component: 'Input',
    componentProps: {
      placeholder: '请输入TraceId/名称',
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
    field: 'type',
    label: '日志类型',
    required: true,
    component: 'Select',
    componentProps: {
      options: [
        { label: '日志', value: '1' },
        { label: '异常', value: '2' },
      ],
    },
    dynamicDisabled: true,
  },
  {
    field: 'title',
    label: '日志标题',
    component: 'Input',
  },
  {
    field: 'method',
    label: '执行方法',
    component: 'Input',
  },
  {
    field: 'url',
    label: '请求路径',
    component: 'Input',
  },
  {
    field: 'params',
    label: '请求参数',
    component: 'InputTextArea',
  },
  {
    label: '操作内容',
    field: 'operation',
    component: 'InputTextArea',
  },
  {
    field: 'exception',
    label: '异常信息',
    component: 'InputTextArea',
  }
];
