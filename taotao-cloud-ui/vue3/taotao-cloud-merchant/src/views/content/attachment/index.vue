<template>
  <PageWrapper dense contentFullHeight fixedHeight contentClass="flex">
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button type="primary" @click="handleCreate">新增文件</a-button>
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
      <template #img="{ record }">
        <TableImg :size="60" :simpleShow="true" :imgList="record.url.split(' ')" />
      </template>
      <template #fileSize="{ record }">
        {{ getFileSize(record.size) }}
      </template>
    </BasicTable>
    <AttachmentDrawer @register="registerDrawer" @success="handleSuccess" />
  </PageWrapper>
</template>
<script lang="ts" setup>
  // 引入基础组件
  import { PageWrapper } from '/@/components/Page';
  import { BasicTable, useTable, TableAction, TableImg } from '/@/components/Table';
  // 插入数据内容
  import { columns, searchFormSchema } from './attachment.data';
  // 通过API接口获取日志
  import { page, del } from '/@/api/content/attachment';

  import { useDrawer } from '/@/components/Drawer';
  import AttachmentDrawer from './AttachmentDrawer.vue';

  import { useMessage } from '/@/hooks/web/useMessage';
  import { getFileSize } from '/@/utils/file/download';
  const { createMessage } = useMessage();

  const [registerDrawer, { openDrawer }] = useDrawer();
  const [registerTable, { reload }] = useTable({
    title: '文件列表',
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

  function handleCreate() {
    openDrawer(true, {
      isUpdate: false,
    });
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

  function handleSuccess() {
    reload();
  }
</script>
