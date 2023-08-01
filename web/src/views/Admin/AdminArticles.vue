<template>
  <a-layout :style="{padding: '0 50px',minHeight: '280px'}">
    <a-layout-content style="padding: 24px 0; background: #fff">
      <div class="addAndSearch">
        <router-link to="/admin/editor">
          <a-button type="primary" style="margin-left:20px ">
            新增
          </a-button>
        </router-link>


        <a-form
            layout="inline"
            :model="keyword"
            class="searchKeyword"
        >
          <a-form-item>
            <a-input v-model:value="keyword" placeholder="请输入需要查询的书名"></a-input>
          </a-form-item>
          <a-form-item>
            <a-button type="primary" @click="search">搜索</a-button>
          </a-form-item>
        </a-form>
      </div>

      <a-table
          :columns="columns"
          :data-source="articless"
          row-key="id"
          :pagination="pagination"
          :loading="loading"
          @change="handleTableChange"
      >
        <template #cover="{ text:cover }">
          <img class="img" v-if="cover" :src="cover" alt="avatar"/>
        </template>
        <template v-slot:category1Id="{text, record }">
          {{getCategoryName(record.category1Id)}}
        </template>

        <template v-slot:category2Id="{text, record }">
          {{getCategoryName(record.category2Id)}}
        </template>

        <template v-slot:action="{ text, record }">
          <a-space size="middle">
            <router-link :to="'/admin/editor?articlesId=' + record.id">
              <a-button type="primary">编辑</a-button>
            </router-link>

            <a-popconfirm
                title="Are you sure delete this articles?"
                ok-text="Yes"
                cancel-text="No"
                @confirm="showDeleteConfirm(record.id,record.name)"
            >
              <a-button type="primary" danger >删除</a-button>

            </a-popconfirm>

          </a-space>
        </template>
      </a-table>
    </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
import {createVNode, defineComponent, onMounted, ref} from 'vue';
import axios from 'axios';
import {message, Modal} from "ant-design-vue";
import {Tool} from "@/util/tool";
import {ExclamationCircleOutlined} from "@ant-design/icons-vue";

export default defineComponent({
  name: 'AdminArticles',
  setup() {
    const articless = ref();
    const keyword = ref();
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
        slots: {customRender: 'cover'},
        width:'200px'
      },
      {
        title: '名称',
        dataIndex: 'name',
        width:'200px'
      },
      {
        title: '分类一',
        dataIndex: 'category1Id',
        slots: {customRender: 'category1Id'},
        width:'200px'
      },
      {
        title: '分类二',
        dataIndex: 'category2Id',
        slots: {customRender: 'category2Id'},
        width:'200px'
      },
      {
        title: '阅读数',
        dataIndex: 'viewCount',
        width:'200px'
      },
      {
        title: '点赞数',
        dataIndex: 'voteCount',
        width:'200px'
      },
      {
        title: 'Action',
        key: 'action',
        width:'400px',
        slots: {customRender: 'action'}
      }
    ];
    const open = ref<boolean>(false);
    const confirmLoading = ref<boolean>(false);
    const listCategory = ref();
    let categorys:any;
    const search = ()=> {
        handleQuery(
            {
              name:keyword.value,
              page: 1,
              size: pagination.value.pageSize
            }
        )
    }


    // 数据查询
    const handleQuery = (params: any) => {
      loading.value = true;
      axios.get("/articles/list", {
        params:params
      }).then((response) => {
        loading.value = false;
        const data = response.data;
        articless.value = data.content.list;
        //
        //重置分页按钮
        pagination.value.current = params.page;
        pagination.value.total = data.content.total;
      });
    };
    const handleTableChange = (pagination: any) => {
      handleQuery({
        page: pagination.current,
        size: pagination.pageSize
      });
    };
    const QueryCategorysAndArticless = () => {
      axios.get("/category/list").then(
          (response) => {
            const data = response.data;
            categorys = data.content.list;
            listCategory.value = [];
            listCategory.value = Tool.array2Tree(categorys,0);
            handleQuery({
              page: pagination.value.current,
              size: pagination.value.pageSize
            });
          }
      )
    }
    const getCategoryName  = (cid:number) => {
      let result = "";
      categorys.forEach((item:any)=>{
        if(Number(item.id) === Number(cid)){
          result = item.name;
        }
      });
      return result;
    }


    const showDeleteConfirm = (id:number,name:string) => {
      Modal.confirm({
        title: 'Are you sure delete this article?',
        icon: createVNode(ExclamationCircleOutlined),
        content: name,
        okText: 'Yes',
        okType: 'danger',
        cancelText: 'No',
        onOk() {
          DeleteArticles(id);
        },
      });
    };

    const DeleteArticles = (id: number) => {
      axios.delete("/articles/delete/" + id).then((response) => {
        const data = response.data;
        if (data.success) {
          //重新加载页面
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize
          });
        }
      })
    }

    onMounted(() => {
      QueryCategorysAndArticless();
    })

    return {
      articless,
      pagination,
      columns,
      loading,
      confirmLoading,
      open,
      keyword,
      listCategory,

      handleTableChange,
      search,
      showDeleteConfirm,
      getCategoryName


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
.addAndSearch{
  margin-bottom: 20px;
}
.searchKeyword {
  display: flex;
  float: right;
}
</style>