<template>
  <PageWrapper dense contentFullHeight fixedHeight contentClass="flex">
    <RouteTree class="w-1/4 xl:w-1/5" @select="handleSelect" />
    <BasicTable @register="registerTable" class="w-3/4 xl:w-4/5" :searchInfo="searchInfo">
      <template #toolbar>
        <a-button type="primary" @click="handleSync">同步API</a-button>
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
    <ApiDrawer @register="registerDrawer" @success="handleSuccess" />
  </PageWrapper>
</template>
<script lang="ts" setup>
  import { reactive } from 'vue';
  // 引入基础组件
  import { PageWrapper } from '/@/components/Page';
  import { BasicTable, useTable, TableAction } from '/@/components/Table';
  // 插入数据内容
  import { columns, searchFormSchema } from './api.data';
  // 通过API接口获取日志
  import { page, sync, del } from '/@/api/gateway/api';

  import { useDrawer } from '/@/components/Drawer';
  import ApiDrawer from './ApiDrawer.vue';

  import { useMessage } from '/@/hooks/web/useMessage';
  import RouteTree from './RouteTree.vue';
  const { createMessage } = useMessage();
  const searchInfo = reactive<Recordable>({});

  const [registerDrawer, { openDrawer }] = useDrawer();
  const [registerTable, { reload }] = useTable({
    title: '微服务列表',
    api: page,
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

  // function handleCreate() {
  //   openDrawer(true, {
  //     isUpdate: false,
  //   });
  // }
  async function handleSync() {
    await sync();
    handleSuccess();
  }
  function handleEdit(record: Recordable) {
    openDrawer(true, {
      record,
      isUpdate: true,
    });
  }

  async function handleDelete(record: Recordable) {
    await del({ ids: record.id });
    createMessage.success('删除成功!');
    handleSuccess();
  }

  function handleSelect(serviceId = '') {
    searchInfo.serviceId = serviceId;
    reload();
  }

  function handleSuccess() {
    reload();
  }
</script>
