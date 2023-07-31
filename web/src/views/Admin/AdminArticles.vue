<template>
  <a-layout :style="{padding: '0 50px',minHeight: '280px'}">
    <a-breadcrumb style="margin: 16px 0">
      <a-breadcrumb-item>
        <a href="/">首页</a>
      </a-breadcrumb-item>
      <a-breadcrumb-item>
        <router-link to="/admin/articless">电子书管理</router-link>
      </a-breadcrumb-item>
    </a-breadcrumb>
    <a-layout-content style="padding: 24px 0; background: #fff">
      <div class="addAndSearch">
        <a-button type="primary" @click="add" style="margin-left:20px ">
          新增
        </a-button>

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
            <a-button type="primary" @click="edit(record)">编辑</a-button>
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

  <a-modal v-model:open="open" title="电子书" :confirm-loading="confirmLoading" @ok="handleOk">
    <a-form
        :model="articles"
        name="basic"
        :label-col="{ span: 4 }"
        :wrapper-col="{ span: 18 }"
        autocomplete="on"
    >
      <!--:rules="[{ required: true, message: 'Please input your username!' }]"-->
      <a-form-item label=封面>
        <a-input v-model:value="articles.cover"/>
      </a-form-item>

      <a-form-item label=名称>
        <a-input v-model:value="articles.name"/>
      </a-form-item>


      <a-form-item label=分类>
        <a-cascader
            v-model:value="categoryId"
            :options="listCategory"
            :field-names="{label:'name' , value:'id' , children:'children'}"
        ></a-cascader>

      </a-form-item>

      <a-form-item label=描述>
        <a-input v-model:value="articles.description"/>
      </a-form-item>

    </a-form>
  </a-modal>
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
    const articles = ref();
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
    const categoryId = ref();
    const listCategory = ref();
    let categorys:any;
    const edit = (record: any) => {
      open.value = true;
      articles.value = Tool.copy(record);
      categoryId.value = [getCategoryName(articles.value.category1Id),getCategoryName(articles.value.category2Id)];
      if(categoryId.value[0] === ''){
        categoryId.value = [];
      }
    };
    const add = () => {
      open.value = true;
      articles.value = {};
      categoryId.value = [];
    }
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
    const handleOk = () => {
      confirmLoading.value = true;
      articles.value.category1Id = getCategoryId(categoryId.value[0]);
      articles.value.category2Id = getCategoryId(categoryId.value[1]);
      axios.post("/articles/save", articles.value).then(
          (response) => {
            const data = response.data;
            if (data.success) {
              open.value = false;
              confirmLoading.value = false;
              //重新加载页面
              handleQuery({
                name:keyword.value,
                page: pagination.value.current,
                size: pagination.value.pageSize
              });
            } else {
              confirmLoading.value = false;
              message.error(data.message);
            }
          }
      )

    };
    const getCategoryName  = (cid:number) => {
      let result = "";
      categorys.forEach((item:any)=>{
        if(Number(item.id) === Number(cid)){
          result = item.name;
        }
      });
      return result;
    }

    const getCategoryId = (name:string)=>{
      let result = "";
      categorys.forEach((item:any)=>{
        console.log()
        if(item.name === name){
          result = item.id;
        }
      });
      if(result === ''){
        //本身就是id
        return name;
      }
      else return result;
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
      articles,
      keyword,
      categoryId,
      listCategory,

      handleTableChange,
      edit,
      add,
      search,
      handleOk,
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