<template>
  <div class="not-enough">
    <ul class="nav-bar">
      <li
        v-for="(item, index) in conData.options.navList"
        :class="currentIndex === index ? 'curr' : ''"
        @click="changeCurr(index)"
        :key="index"
      >
        <p>{{ item.title }}</p>
        <p>{{ item.desc }}</p>
      </li>
    </ul>
    <div class="content" v-if="showContent">
      <div
        v-for="(item, index) in conData.options.list[currentIndex]"
        :key="index"
        class="hover-pointer"
        @click="linkTo(item.url)"
      >
        <img :src="item.img" width="210" height="210" :alt="item.name" />
        <p>{{ item.name }}</p>
        <p>
          <span>{{ Number(item.price) | unitPrice("￥") }}</span>
        </p>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  props: {
    data: {
      type: Object,
      default: null
    }
  },
  data () {
    return {
      currentIndex: 0, // 当前分类下标
      conData: this.data, // 装修数据
      showContent: true // 是否展示内容
    };
  },
  watch: {
    data: function (val) {
      this.conData = val;
    },
    conData: function (val) {
      this.$emit('content', val);
    }
  },
  methods: {
    changeCurr (index) { // 选择分类
      this.currentIndex = index;
    }
  }
};
</script>
<style lang="scss" scoped>
.nav-bar {
  display: flex;
  justify-content: center;
  width: 100%;
  margin-bottom: 10px;
  background-color: rgb(218, 217, 217);
  height: 60px;
  align-items: center;
  position: relative;
  li {
    padding: 0 30px;
    text-align: center;
    p:nth-child(1) {
      font-size: 16px;
      border-radius: 50px;
      padding: 0 7px;
    }

    p:nth-child(2) {
      font-size: 14px;
      color: #999;
    }

    &:hover {
      p {
        color: $theme_color;
      }
      cursor: pointer;
    }
    border-right: 1px solid #eee;
  }
  li:last-of-type {
    border: none;
  }

  .curr {
    p:nth-child(1) {
      background-color: $theme_color;

      color: #fff;
    }
    p:nth-child(2) {
      color: $theme_color;
    }
  }
}

.content {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  > div {
    padding: 10px;
    box-sizing: border-box;
    border: 1px solid #eee;
    margin-bottom: 10px;

    &:hover {
      border-color: $theme_color;
      color: $theme_color;
    }

    p:nth-of-type(1) {
      overflow: hidden;
      width: 210px;
      white-space: nowrap;
      text-overflow: ellipsis;
      margin: 10px 0 5px 0;
    }
    p:nth-of-type(2) {
      color: $theme_color;
      font-size: 16px;
      display: flex;
      justify-content: space-between;
      align-items: center;
      span:nth-child(2) {
        text-decoration: line-through;
        font-size: 12px;
        color: #999;
      }
    }
  }
}
</style>
