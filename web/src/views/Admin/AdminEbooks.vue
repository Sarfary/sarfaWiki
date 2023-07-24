<template>
  <a-layout :style="{padding: '0 50px',minHeight: '280px'}">
    <a-layout-content style="padding: 24px 0; background: #fff">

      <p>
        <a-button type="primary" @click="add" style="margin-left:20px " >
          新增
        </a-button>
      </p>

      <a-table
          :columns="columns"
          :data-source="ebooks"
          row-key="id"
          :pagination="pagination"
          :loading="loading"
          @change="handleTableChange"
      >
        <template #cover="{ text:cover }">
          <img  class = "img" v-if="cover" :src="cover" alt="avatar"/>
        </template>

        <template v-slot:action="{ text, record }">
          <a-space size="middle">
            <a-button type="primary" @click="edit(record)">
              编辑
            </a-button>
            <a-button danger block>
              删除
            </a-button>
          </a-space>
        </template>
      </a-table>
    </a-layout-content>
  </a-layout>

  <a-modal v-model:open="open" title="电子书" :confirm-loading="confirmLoading" @ok="handleOk">
    <a-form
        :model="ebooks"
        name="basic"
        :label-col="{ span: 4 }"
        :wrapper-col="{ span: 18 }"
        autocomplete="on"
    >
      <!--:rules="[{ required: true, message: 'Please input your username!' }]"-->
      <a-form-item label=封面>
        <a-input v-model:value="ebook.cover" />
      </a-form-item>

      <a-form-item label=名称>
        <a-input v-model:value="ebook.name" />
      </a-form-item>

      <a-form-item label=分类一>
        <a-input v-model:value="ebook.category1Id" />
      </a-form-item>

      <a-form-item label=分类二>
        <a-input v-model:value="ebook.category2Id" />
      </a-form-item>

      <a-form-item label=描述>
        <a-input v-model:value="ebook.description" />
      </a-form-item>

    </a-form>
  </a-modal>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue';
import axios from 'axios';

export default defineComponent({
  name: 'AdminEbook',
  setup() {
    const ebooks = ref();
    const ebook = ref();
    const pagination = ref({
      current: 1,
      pageSize: 4,
      total: 0
    });
    const loading = ref(false);
    const columns = [
      {
        title: '封面',
        dataIndex: 'cover',
        slots: {customRender: 'cover'}
      },
      {
        title: '名称',
        dataIndex: 'name'
      },
      {
        title: '分类一',
        dataIndex: 'category1Id',
      },
      {
        title: '分类二',
        dataIndex: 'category2Id',
      },
      {
        title: '文档数',
        dataIndex: 'docCount'
      },
      {
        title: '阅读数',
        dataIndex: 'viewCount'
      },
      {
        title: '点赞数',
        dataIndex: 'voteCount'
      },
      {
        title: 'Action',
        key: 'action',
        slots: {customRender: 'action'}
      }
    ];
    const open = ref<boolean>(false);
    const confirmLoading = ref<boolean>(false);

    const edit = (record : any) => {
      open.value = true;
      ebook.value = record;
    };
    const add = () => {
      open.value = true;
      ebook.value = {};
    }


    // 数据查询
    const handleQuery = (params: any) => {
      loading.value = true;
      axios.get("/ebook/list", {
        params:{
          page:params.page,
          size:params.size
        }
      }).then((response) => {
        loading.value = false;
        const data = response.data;
        ebooks.value = data.content.list;
        //重置分页按钮
        pagination.value.current = params.page;
        pagination.value.total = data.content.total;
      });
    };
    const handleTableChange = (pagination: any) => {
      console.log("看看自带的分页参数都有啥" + pagination);
      handleQuery({
        page: pagination.current,
        size: pagination.pageSize
      });
    };
    const handleOk = () => {
      confirmLoading.value = true;
      axios.post("/ebook/save",ebook.value).then(
          (response)=>{
            const data = response.data;
            if(data.success){
              open.value = false;
              confirmLoading.value = false;
              console.log(pagination.value);
              //重新加载页面
              handleQuery({
                page: pagination.value.current,
                size: pagination.value.pageSize
              });
            }
          }
      )

    };

    onMounted(() => {
      handleQuery({
        page:pagination.value.current,
        size:pagination.value.pageSize

      });
    })

    return {
      ebooks,
      pagination,
      columns,
      loading,
      confirmLoading,
      open,
      ebook,
      handleTableChange,
      edit,
      add,
      handleOk


    }

  }
});
</script>

<style scoped>

.img {
  width: 100px;
  vertical-align: middle;
  border-style: none;
}
</style>