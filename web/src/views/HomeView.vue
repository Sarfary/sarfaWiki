<template>
  <a-layout>
    <a-layout-content :style="{padding: '0 50px'}">
      <a-layout style="padding: 24px 0; background: #fff">
        <a-layout-sider width="200" style="background: #fff">
          <a-menu
              mode="inline"
              style="height: 730px"
          >
            <a-menu-item key="welcome">
              <router-link :to="'/'">
                <MailOutLined/><span>欢迎</span>
              </router-link>
            </a-menu-item>
            <a-sub-menu v-for="item in listCategory" :key="item.id">
              <template v-slot:title>
                <span><user-outlined/>{{item.name}}</span>
              </template>
              <a-menu-item v-for="child in item.children" :key="child.id">
                <MailOutLined/><span>{{child.name}}</span>
              </a-menu-item>
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
                    <a-avatar :src="item.cover" shape="square" :size="100"/>
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
import {Tool} from "@/util/tool";

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
    const listCategory = ref();
    const categorys = ref();
    const QueryCategorys = (params:any) => {
      axios.get("/category/list", {
        params:params
      }).then((response) => {
        const data = response.data;
        if(data.success){
          categorys.value = data.content.list;
          listCategory.value = [];
          listCategory.value = Tool.array2Tree(categorys.value,0);
        }
        else {
          message.error(data.message);
        }

      });
    }
    const QueryEbooks = (parms:any) => {
      axios.get("/ebook/list",{
        params:parms
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
    }

    // 生命周期函数
    onMounted(() => {
      console.log("onMounted");
      QueryCategorys({});
      QueryEbooks({
        page:pagination.value.current,
        size:pagination.value.pageSize
      });

    });
    return {
      ebooks,
      pagination,
      actions,
      listCategory
    }

  }
});
</script>
