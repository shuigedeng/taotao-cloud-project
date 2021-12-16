<template>
  <div class="bg-white m-4 mr-0 overflow-hidden">
    <BasicTree
      title="微服务列表"
      toolbar
      search
      :clickRowToExpand="false"
      :treeData="treeData"
      :replaceFields="{ key: 'id', title: 'name' }"
      @select="handleSelect"
    />
  </div>
</template>
<script lang="ts">
  import { defineComponent, onMounted, ref } from 'vue';

  import { BasicTree, TreeItem } from '/@/components/Tree';
  import { tree } from '/@/api/gateway/route';
  import { findNode } from '/@/utils/helper/treeHelper';

  export default defineComponent({
    name: 'DeptTree',
    components: { BasicTree },

    emits: ['select'],
    setup(_, { emit }) {
      const treeData = ref<TreeItem[]>([]);

      async function fetch() {
        treeData.value = (await tree()) as unknown as TreeItem[];
      }

      function handleSelect(keys: string) {
        const node = findNode(treeData.value, (item) => item.id === keys[0], {
          id: 'id',
        });
        emit('select', node.name);
      }

      onMounted(() => {
        fetch();
      });
      return { treeData, handleSelect };
    },
  });
</script>
