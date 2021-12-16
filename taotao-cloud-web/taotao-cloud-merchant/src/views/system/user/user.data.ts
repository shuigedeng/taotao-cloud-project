import { roleAllList } from './../../../api/system/role';
import { isAccountExist } from '/@/api/demo/system';
import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
import { h } from 'vue';
import { Tag } from 'ant-design-vue';
import { number } from 'vue-types';

export const columns: BasicColumn[] = [
  {
    title: '用户名',
    dataIndex: 'account',
    width: 120,
  },
  {
    title: '昵称',
    dataIndex: 'name',
    width: 120,
  },
  {
    title: '邮箱',
    dataIndex: 'email',
    width: 120,
  },
  {
    title: '角色',
    dataIndex: 'roleName',
    width: 200,
  },
  {
    title: '状态',
    dataIndex: 'status',
    width: 100,
    customRender: ({ record }) => {
      const status = record.status;
      const enable = status === '0';
      const color = enable ? 'green' : 'red';
      const text = enable ? '在职' : '离职';
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
      placeholder: '请输入名称/编码',
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

export const accountFormSchema: FormSchema[] = [
  {
    field: 'id',
    label: 'ID',
    component: 'Input',
    show: false,
  },
  {
    field: 'account',
    label: '用户名',
    component: 'Input',
    helpMessage: ['本字段演示异步验证', '不能输入带有admin的用户名'],
    // rules: [
    //   {
    //     required: true,
    //     message: '请输入用户名',
    //   },
    //   {
    //     validator(_, value) {
    //       return new Promise((resolve, reject) => {
    //         isAccountExist(value)
    //           .then(() => resolve())
    //           .catch((err) => {
    //             reject(err.message || '验证失败');
    //           });
    //       });
    //     },
    //   },
    // ],
  },
  {
    field: 'pwd',
    label: '密码',
    component: 'InputPassword',
    required: true,
    ifShow: false,
  },
  {
    field: 'sex',
    label: '性别',
    component: 'RadioButtonGroup',
    defaultValue: 1,
    componentProps: {
      options: [
        { label: '男', value: 1 },
        { label: '女', value: 2 },
      ],
    },
  },
  {
    label: '授权角色',
    field: 'roleId',
    component: 'ApiSelect',
    componentProps: {
      api: roleAllList,
      resultField: 'list',
      labelField: 'roleName',
      valueField: 'id',
    },
    required: true,
  },
  {
    field: 'departId',
    label: '所属部门',
    component: 'TreeSelect',
    componentProps: {
      replaceFields: {
        title: 'name',
        key: 'id',
        value: 'id',
      },
      getPopupContainer: () => document.body,
    },
    required: true,
  },
  {
    field: 'realName',
    label: '姓名',
    component: 'Input',
    required: true,
  },
  {
    field: 'name',
    label: '昵称',
    component: 'Input',
    required: true,
  },

  {
    label: '邮箱',
    field: 'email',
    component: 'Input',
    required: true,
  },
  {
    field: 'status',
    label: '状态',
    component: 'RadioButtonGroup',
    defaultValue: '0',
    componentProps: {
      options: [
        { label: '在职', value: '0' },
        { label: '离职', value: '1' },
      ],
    },
  },
  {
    label: '备注',
    field: 'remark',
    component: 'InputTextArea',
  },
];

export const passwordFormSchema: FormSchema[] = [
  {
    field: 'id',
    label: 'ID',
    component: 'Input',
    show: false,
  },
  {
    field: 'passwordNew',
    label: '密码',
    component: 'StrengthMeter',
    componentProps: {
      placeholder: '密码',
    },
    rules: [
      {
        required: true,
        whitespace: true,
        message: '请输入密码！',
      },
      {
        pattern: new RegExp('[^\\u4e00-\\u9fa5]+'),
        type: 'string',
        message: '密码不能输入汉字！',
      },
      {
        min: 6,
        max: 32,
        message: '长度必需在6-32之间！',
      },
    ],
  },
  {
    field: 'confirmPassword',
    label: '确认密码',
    component: 'InputPassword',

    dynamicRules: ({ values }) => {
      return [
        {
          required: true,
          validator: (_, value) => {
            if (!value) {
              return Promise.reject('确认密码不能为空');
            }
            if (value !== values.passwordNew) {
              return Promise.reject('两次输入的密码不一致!');
            }
            return Promise.resolve();
          },
        },
        {
          pattern: new RegExp('[^\\u4e00-\\u9fa5]+'),
          type: 'string',
          message: '密码不能输入汉字！',
        },
        {
          min: 6,
          max: 32,
          message: '长度必需在6-32之间！',
        },
      ];
    },
  },
];
