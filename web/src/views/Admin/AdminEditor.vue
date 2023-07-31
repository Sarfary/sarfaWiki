<template>
  <a-layout :style="{padding: '0 50px',minHeight: '280px'}">
    <a-breadcrumb style="margin: 16px 0">
      <a-breadcrumb-item>
        <a href="/">首页</a>
      </a-breadcrumb-item>
      <a-breadcrumb-item>
        <router-link to="/admin/ebooks">电子书管理</router-link>
      </a-breadcrumb-item>
<!--      <a-breadcrumb-item>-->
<!--        <router-link :to="'/admin/doc?ebookId=' + ebookId">文档管理</router-link>-->
<!--      </a-breadcrumb-item>-->
    </a-breadcrumb>
    <md-editor v-model="text" class="AdminEditor"></md-editor>
  </a-layout>
</template>

<script  lang="ts">
import {defineComponent, ref} from 'vue';
import {MdEditor} from "md-editor-v3";
import "md-editor-v3/lib/style.css";
export default defineComponent({
  name: 'AdminEditor',
  components:{
    MdEditor,
  },
  setup(){
    const text = ref();


    return{
      text
    }
  }
});
</script>

<style scoped>
.AdminEditor{
  height: 730px;

}
</style>