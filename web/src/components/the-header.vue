<template>
  <a-layout id="the-header-style">
    <a-layout-header class="header">
      <div class="logo"/>
      <a-menu
          v-model:selectedKeys="selectedKeys1"
          theme="dark"
          mode="horizontal"
          :style="{ lineHeight: '64px' }"
      >
        <a-menu-item key="/">
          <router-link to="/">首页</router-link>
        </a-menu-item>
        <a-menu-item key="/admin/ebooks">
          <router-link to="/admin/ebooks">电子书管理</router-link>
        </a-menu-item>
        <a-menu-item key="/about">
          <router-link to="/about">关于我们</router-link>
        </a-menu-item>
      </a-menu>
    </a-layout-header>
  </a-layout>
</template>

<script lang="ts">
import {defineComponent} from 'vue';

export default defineComponent({
  name: 'the-header',
});
</script>

<style scoped>
#the-header-style .logo {
  float: left;
  width: 120px;
  height: 31px;
  margin: 16px 24px 16px 0;
  background: rgba(255, 255, 255, 0.3);
}

.ant-row-rtl #cthe-header-style .logo {
  float: right;
  margin: 16px 0 16px 24px;
}
</style>
