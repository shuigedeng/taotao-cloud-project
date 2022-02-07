<template>
  <PageWrapper dense contentFullHeight fixedHeight contentClass="flex">
    <DeptTree class="w-1/4 xl:w-1/5" @select="handleSelect" />
    <BasicTable @register="registerTable" class="w-3/4 xl:w-4/5" :searchInfo="searchInfo">
      <template #toolbar>
        <a-button type="primary" @click="handleCreate">新增用户</a-button>
      </template>
      <template #action="{ record }">
        <TableAction
          :actions="[
            {
              icon: 'clarity:lock-line',
              tooltip: '修改密码',
              onClick: handleSetPassword.bind(null, record),
            },
            {
              icon: 'clarity:note-edit-line',
              tooltip: '编辑用户资料',
              onClick: handleEdit.bind(null, record),
            },
            {
              icon: 'ant-design:delete-outlined',
              color: 'error',
              tooltip: '删除此账号',
              popConfirm: {
                title: '是否确认删除',
                confirm: handleDelete.bind(null, record),
              },
            },
          ]"
        />
      </template>
    </BasicTable>
    <UserModal @register="registerModal" @success="handleSuccess" />
    <PasswordModal @register="registerPasswordModal" @success="handlePasswordSuccess" />
  </PageWrapper>
</template>
<script lang="ts">
  import { defineComponent, reactive } from 'vue';

  import { BasicTable, useTable, TableAction } from '/@/components/Table';
  import { del, page } from '/@/api/system/user';
  import { PageWrapper } from '/@/components/Page';
  import DeptTree from './DeptTree.vue';

  import { useModal } from '/@/components/Modal';
  import UserModal from './UserModal.vue';
  import PasswordModal from './PasswordModal.vue';

  import { columns, searchFormSchema } from './user.data';
  import { useGo } from '/@/hooks/web/usePage';
  import { useMessage } from '/@/hooks/web/useMessage';
  const { createMessage } = useMessage();

  export default defineComponent({
    name: 'UserManagement',
    components: { BasicTable, PageWrapper, DeptTree, UserModal, TableAction, PasswordModal },
    setup() {
      const go = useGo();
      const [registerModal, { openModal }] = useModal();
      const [registerPasswordModal, { openModal: openPasswordModal }] = useModal();
      const searchInfo = reactive<Recordable>({});
      const [registerTable, { reload, updateTableDataRecord }] = useTable({
        title: '用户列表',
        api: page,
        rowKey: 'id',
        columns,
        formConfig: {
          labelWidth: 120,
          schemas: searchFormSchema,
          autoSubmitOnEnter: true,
        },
        useSearchForm: true,
        showTableSetting: true,
        bordered: true,
        handleSearchInfoFn(info) {
          console.log('handleSearchInfoFn', info);
          return info;
        },
        actionColumn: {
          width: 120,
          title: '操作',
          dataIndex: 'action',
          slots: { customRender: 'action' },
        },
      });

      function handleCreate() {
        openModal(true, {
          isUpdate: false,
        });
      }

      function handleEdit(record: Recordable) {
        openModal(true, {
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

      function handleSelect(departId = '') {
        searchInfo.departId = departId;
        reload();
      }

      function handleSetPassword(record: Recordable) {
        openPasswordModal(true, {
          record,
          isUpdate: true,
        });
      }

      function handleView(record: Recordable) {
        go('/system/account_detail/' + record.id);
      }

      function handlePasswordSuccess() {
        reload();
      }

      return {
        registerTable,
        registerModal,
        handleCreate,
        handleEdit,
        handleDelete,
        handleSuccess,
        handleSelect,
        handleView,
        searchInfo,
        handleSetPassword,
        registerPasswordModal,
        handlePasswordSuccess,
      };
    },
  });
</script>
