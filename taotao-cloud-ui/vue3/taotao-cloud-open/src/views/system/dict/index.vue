<template>
  <PageWrapper dense contentFullHeight fixedHeight contentClass="flex">
    <BasicTable @register="registerTable" @row-click="clickSubTable" class="w-2/4 xl:w-2/4">
      <template #toolbar>
        <a-button type="primary" @click="handleCreate">新增字典</a-button>
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
    <DictSubTable ref="dictSubRef" class="w-2/4 xl:w-2/4" />
    <DictDrawer @register="registerDrawer" @success="handleSuccess" />
  </PageWrapper>
</template>
<script lang="ts">
  import { defineComponent, ref } from 'vue';
  // 引入基础组件
  import { PageWrapper } from '/@/components/Page';
  import { BasicTable, useTable, TableAction } from '/@/components/Table';
  // 插入数据内容
  import { columns, searchFormSchema } from './dict.data';
  // 通过API接口获取日志
  import { page, del } from '/@/api/system/dict';

  import { useDrawer } from '/@/components/Drawer';
  import DictDrawer from './DictDrawer.vue';

  import DictSubTable from './DictSubTable.vue';

  import { useMessage } from '/@/hooks/web/useMessage';
  export default defineComponent({
    components: { BasicTable, PageWrapper, DictDrawer, DictSubTable, TableAction },
    setup() {
      const { createMessage } = useMessage();
      const dictSubRef = ref();
      const [registerDrawer, { openDrawer }] = useDrawer();
      const [registerTable, { reload }] = useTable({
        title: '字典列表',
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

      function clickSubTable(record: Recordable) {
        dictSubRef.value.filterByDictCode(record);
      }

      function handleSuccess() {
        reload();
      }

      return {
        registerTable,
        registerDrawer,
        clickSubTable,
        handleCreate,
        handleEdit,
        handleDelete,
        handleSuccess,
        dictSubRef,
      };
    },
  });
</script>
