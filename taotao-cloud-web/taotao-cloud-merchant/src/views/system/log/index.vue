<template>
  <PageWrapper dense contentFullHeight fixedHeight contentClass="flex">
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button type="primary" @click="handleDeleteAll">清空日志</a-button>
      </template>
      <template #action="{ record }">
        <TableAction
          :actions="[
            {
              icon: 'clarity:note-edit-line',
              onClick: handleEdit.bind(null, record),
            },
            {
              icon: 'ant-design:delete-outlined',
              color: 'error',
              popConfirm: {
                title: '是否确认删除',
                confirm: handleDelete.bind(null, record),
              },
            },
          ]"
        />
      </template>
    </BasicTable>
    <LogDrawer @register="registerDrawer" @success="handleSuccess" />
  </PageWrapper>
</template>
<script lang="ts" setup>
  // 引入基础组件
  import { PageWrapper } from '/@/components/Page';
  import { BasicTable, useTable, TableAction } from '/@/components/Table';
  // 插入数据内容
  import { columns, searchFormSchema } from './log.data';
  // 通过API接口获取日志
  import { getLogListByPage, logEmpty } from '/@/api/system/log';

  import { useDrawer } from '/@/components/Drawer';
  import LogDrawer from './LogDrawer.vue';

  import { useMessage } from '/@/hooks/web/useMessage';
  const { createConfirm } = useMessage();

  const [registerDrawer, { openDrawer }] = useDrawer();
  const [registerTable, { reload }] = useTable({
    title: '日志列表',
    api: getLogListByPage,
    columns,
    formConfig: {
      labelWidth: 120,
      schemas: searchFormSchema,
    },
    useSearchForm: true,
    showTableSetting: true,
    bordered: true,
    showIndexColumn: false,
    actionColumn: {
      width: 80,
      title: '操作',
      dataIndex: 'action',
      slots: { customRender: 'action' },
      fixed: undefined,
    },
  });

  function handleEdit(record: Recordable) {
    openDrawer(true, {
      record,
      isUpdate: true,
    });
  }

  function handleDelete(record: Recordable) {
    console.log(record);
  }

  function handleDeleteAll() {
    createConfirm({
      iconType: 'warning',
      content: '确定要清空所有日志数据吗？',
      onOk: async () => {
        await logEmpty();
        reload();
      },
    });
  }

  function handleSuccess() {
    reload();
  }
</script>
