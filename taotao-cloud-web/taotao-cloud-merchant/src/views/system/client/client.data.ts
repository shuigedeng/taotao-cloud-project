import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';

export const columns: BasicColumn[] = [
  {
    title: '客户端Id',
    dataIndex: 'clientId',
    width: 100,
  },
  {
    title: '客户端密钥',
    dataIndex: 'clientSecret',
    width: 100,
  },
  {
    title: '授权类型',
    dataIndex: 'authorizedGrantTypes',
    width: 130,
  },
  {
    title: '授权范围',
    dataIndex: 'scope',
    width: 90,
  },
  {
    title: '令牌过期秒数',
    dataIndex: 'accessTokenValidity',
    width: 130,
  },
  {
    title: '令牌过期秒数',
    dataIndex: 'refreshTokenValidity',
    width: 130,
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
      placeholder: '请输入客户端Id/编码',
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
    field: 'clientId',
    label: '客户端Id',
    component: 'Input',
  },
  {
    field: 'clientSecret',
    label: '客户端密钥',
    component: 'Input',
  },
  {
    field: 'authorizedGrantTypes',
    label: '授权类型',
    component: 'InputTextArea',
    colProps: { lg: 24, md: 24 },
  },
  {
    field: 'scope',
    label: '授权范围',
    component: 'Input',
  },
  {
    field: 'accessTokenValidity',
    label: '过期秒数',
    component: 'Input',
  },
  {
    field: 'refreshTokenValidity',
    label: '刷新秒数',
    component: 'Input',
  },
  {
    field: 'webServerRedirectUri',
    label: '回调地址',
    component: 'Input',
  },
  {
    field: 'additionalInformation',
    label: '附加说明',
    component: 'InputTextArea',
    colProps: { lg: 24, md: 24 },
  },
];
