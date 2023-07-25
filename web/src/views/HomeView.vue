<template>
  <a-layout>
    <a-layout-content :style="{padding: '0 50px'}">
      <a-layout style="padding: 24px 0; background: #fff">
        <a-layout-sider width="200" style="background: #fff">
          <a-menu
              v-model:selectedKeys="selectedKeys2"
              v-model:openKeys="openKeys"
              mode="inline"
              style="height: 100%"
          >
            <a-sub-menu key="sub1">
              <template #title>
                <span>
                  <user-outlined/>
                  subnav 1
                </span>
              </template>
              <a-menu-item key="1">option1</a-menu-item>
              <a-menu-item key="2">option2</a-menu-item>
              <a-menu-item key="3">option3</a-menu-item>
              <a-menu-item key="4">option4</a-menu-item>
            </a-sub-menu>
            <a-sub-menu key="sub2">
              <template #title>
                <span>
                  <laptop-outlined/>
                  subnav 2
                </span>
              </template>
              <a-menu-item key="5">option5</a-menu-item>
              <a-menu-item key="6">option6</a-menu-item>
              <a-menu-item key="7">option7</a-menu-item>
              <a-menu-item key="8">option8</a-menu-item>
            </a-sub-menu>
            <a-sub-menu key="sub3">
              <template #title>
                <span>
                  <notification-outlined/>
                  subnav 3
                </span>
              </template>
              <a-menu-item key="9">option9</a-menu-item>
              <a-menu-item key="10">option10</a-menu-item>
              <a-menu-item key="11">option11</a-menu-item>
              <a-menu-item key="12">option12</a-menu-item>
            </a-sub-menu>
          </a-menu>
        </a-layout-sider>
        <a-layout-content :style="{ padding: '0 24px', minHeight: '280px' }">
          <a-list item-layout="vertical" size="large" :grid="{ gutter: 20, column: 3 }"
                  :pagination="pagination" :data-source="ebooks">
            <template #renderItem="{ item }">
              <a-list-item key="item.name">
                <template #actions>
                  <span v-for="{ icon, text } in actions" :key="icon">
                    <component :is="icon" style="margin-right: 8px"/>
                    {{ text }}
                  </span>
                </template>
                <a-list-item-meta :description="item.description">
                  <template #title>
                    <a :href="item.href">{{ item.name }}</a>
                  </template>
                  <template #avatar>
                    <a-avatar :src="item.avatar" shape="square" :size="100"/>
                  </template>
                </a-list-item-meta>

              </a-list-item>
            </template>
          </a-list>
        </a-layout-content>
      </a-layout>
    </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue';
import axios from "axios";
import {StarOutlined, LikeOutlined, MessageOutlined} from '@ant-design/icons-vue';
import {message} from "ant-design-vue";

export default defineComponent({
  name: 'HomeView',
  components: {},
  setup() {
    console.log("setup");
    //分页
    const pagination = ref({
      current: 1,
      pageSize: 12,
      total: 0
    });
    const actions: Record<string, any>[] = [
      {icon: StarOutlined, text: '156'},
      {icon: LikeOutlined, text: '156'},
      {icon: MessageOutlined, text: '2'},
    ];
    const ebooks = ref();
    // 生命周期函数
    onMounted(() => {
      console.log("onMounted");
      axios.get("/ebook/list",{
        params:{
          page:pagination.value.current,
          size:pagination.value.pageSize
        }
      }).then(
          res => {
            console.log(res);
            const data = res.data
            if(data.success){
              ebooks.value = data.content.list;
              pagination.value.total = data.content.total;
            }
            else {
              message.error(data.message);
            }
          }
      );
    });
    return {
      ebooks,
      pagination,
      actions
    }

  }
});
</script>
