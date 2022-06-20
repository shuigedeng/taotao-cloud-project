<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>
import {v4 as uuidv4} from 'uuid';
import storage from '@/plugins/storage';
export default {
  name: 'App',
  mounted () {
    let uuid = storage.getItem('uuid');
    if (!uuid) {
      uuid = uuidv4();
      storage.setItem('uuid', uuid);
    }
  }
};
</script>
<style lang="scss">
#app{
  @include background_color($light_background_color);
}
</style>
