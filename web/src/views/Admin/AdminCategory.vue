<template>
  <a-layout :style="{padding: '0 50px',minHeight: '280px'}">
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
            <a-input v-model:value="keyword" placeholder="请输入需要查询的分类"></a-input>
          </a-form-item>
          <a-form-item>
            <a-button type="primary" @click="search">搜索</a-button>
          </a-form-item>
        </a-form>
      </div>

      <a-table
          :columns="columns"
          :data-source="listCategory"
          row-key="id"
          :pagination="false"
          :loading="loading"
          @change="handleTableChange"
      >
        <template v-slot:action="{ text, record }">
          <a-space size="large">
            <a-button type="primary" @click="edit(record)">编辑</a-button>
            <a-popconfirm
                title="Are you sure delete this category?"
                ok-text="Yes"
                cancel-text="No"
                @confirm="DeleteCategory(record.id)"
            >
              <a-button danger block>删除</a-button>

            </a-popconfirm>

          </a-space>
        </template>
      </a-table>
    </a-layout-content>
  </a-layout>

  <a-modal v-model:open="open" title="分类" :confirm-loading="confirmLoading" @ok="handleOk">
    <a-form
        :model="categorys"
        name="basic"
        :label-col="{ span: 4 }"
        :wrapper-col="{ span: 18 }"
        autocomplete="on"
    >
      <!--:rules="[{ required: true, message: 'Please input your username!' }]"-->
      <a-form-item label=名称>
        <a-input v-model:value="category.name"/>
      </a-form-item>

      <a-form-item label=父分类>
        <a-input v-model:value="category.parent"/>
      </a-form-item>

      <a-form-item label=顺序>
        <a-input v-model:value="category.sort"/>
      </a-form-item>

    </a-form>
  </a-modal>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue';
import axios from 'axios';
import {message} from "ant-design-vue";
import {Tool} from "@/util/tool";

export default defineComponent({
  name: 'AdminCategory',
  setup() {
    const categorys = ref();
    const category = ref();
    const keyword = ref();
    const loading = ref(false);
    const listCategory = ref();
    const columns = [

      {
        title: '名称',
        dataIndex: 'name',
        width:'400px'
      },
      {
        title: '父分类',
        dataIndex: 'parent',
        width:'400px'
      },
      {
        title: '顺序',
        dataIndex: 'sort',
        width:'400px'
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

    const edit = (record: any) => {
      open.value = true;
      category.value = Tool.copy(record);
    };
    const add = () => {
      open.value = true;
      category.value = {};
    }
    const search = ()=> {
        handleQuery(
            {
              name:keyword.value,
            }
        )
    }


    // 数据查询
    const handleQuery = (params: any) => {
      loading.value = true;
      axios.get("/category/list", {
        params:params
      }).then((response) => {
        loading.value = false;
        const data = response.data;
        categorys.value = data.content.list;
        listCategory.value = [];
        listCategory.value = Tool.array2Tree(categorys.value,0);
        listCategory.value = Tool.keySearch(listCategory.value,categorys.value);
      });
    };
    const handleTableChange = (pagination: any) => {
      handleQuery({
      });
    };
    const handleOk = () => {
      confirmLoading.value = true;
      axios.post("/category/save", category.value).then(
          (response) => {
            const data = response.data;
            if (data.success) {
              open.value = false;
              confirmLoading.value = false;
              //重新加载页面
              handleQuery({});
            } else {
              confirmLoading.value = false;
              message.error(data.message);
            }
          }
      )

    };
    const DeleteCategory = (id: number) => {
      axios.delete("/category/delete/" + id).then((response) => {
        const data = response.data;
        if (data.success) {
          //重新加载页面
          handleQuery({});
        }
      })
    }

    onMounted(() => {
      handleQuery({});
    })

    return {
      categorys,
      listCategory,
      columns,
      loading,
      confirmLoading,
      open,
      category,
      keyword,
      handleTableChange,
      edit,
      add,
      search,
      handleOk,
      DeleteCategory


    }

  }
});
</script>

<style scoped>
.addAndSearch{
  margin-bottom: 20px;
}
.searchKeyword {
  display: flex;
  float: right;
}
</style>