<template>
  <PageWrapper dense contentFullHeight fixedHeight contentClass="flex">
    <BasicTable @register="registerTable">
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
    <DictSubDrawer @register="registerDrawer" @success="handleSuccess" />
  </PageWrapper>
</template>
<script lang="ts">
  import { defineComponent, ref, reactive } from 'vue';
  // 引入基础组件
  import { PageWrapper } from '/@/components/Page';
  import { BasicTable, useTable, TableAction } from '/@/components/Table';
  // 插入数据内容
  import { columns, subSearchFormSchema } from './dict.data';
  // 通过API接口获取日志
  import { subPage, del } from '/@/api/system/dict';

  import { useDrawer } from '/@/components/Drawer';
  import DictSubDrawer from './DictSubDrawer.vue';

  import { useMessage } from '/@/hooks/web/useMessage';
  export default defineComponent({
    components: { BasicTable, PageWrapper, DictSubDrawer, TableAction },
    setup() {
      const { createMessage } = useMessage();

      let code = ref<string>('');
      let record = reactive({
        code: '',
        parentId: 0,
      });

      const [registerDrawer, { openDrawer }] = useDrawer();
      const [registerTable, { reload, setProps }] = useTable({
        title: '>>字典项列表',
        api: subPage,
        columns,
        formConfig: {
          labelWidth: 120,
          schemas: subSearchFormSchema,
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
        immediate: false,
      });

      function filterByDictCode(records: Recordable) {
        setProps({ searchInfo: { code: records.code } });
        record.code = records.code;
        record.parentId = records.id;
        reload();
      }

      function handleCreate() {
        openDrawer(true, {
          record,
          isUpdate: true,
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
      return {
        registerTable,
        registerDrawer,
        handleCreate,
        handleEdit,
        handleDelete,
        handleSuccess,
        filterByDictCode,
        code,
      };
    },
  });
</script>
