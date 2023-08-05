<template>
  <a-layout :style="{padding: '0 50px',minHeight: '280px'}">
    <a-layout-content style="padding: 24px 0; background: #fff">
      <div class="addAndSearch">
          <a-button type="primary" style="margin-left:20px " @click="add">新增</a-button>
        <a-form
            layout="inline"
            :model="keyword"
            class="searchKeyword"
        >
          <a-form-item>
            <a-input v-model:value="keyword" placeholder="请输入需要查询的登录名"></a-input>
          </a-form-item>
          <a-form-item>
            <a-button type="primary" @click="search">搜索</a-button>
          </a-form-item>
        </a-form>
      </div>

      <a-table
          :columns="columns"
          :data-source="users"
          row-key="id"
          :pagination="pagination"
          :loading="loading"
          @change="handleTableChange"
      >

        <template v-slot:action="{ text, record }">
          <a-space size="middle">
              <a-button type="primary" @click="edit(record)">编辑</a-button>

            <a-popconfirm
                title="Are you sure delete this user?"
                ok-text="Yes"
                cancel-text="No"
                @confirm="showDeleteConfirm(record.id,record.loginName)"
            >
              <a-button type="primary" danger >删除</a-button>

            </a-popconfirm>

          </a-space>
        </template>
      </a-table>

      <a-modal v-model:open="open" title="用户" :confirm-loading="confirmLoading" @ok="handleOk">
        <a-form
            :model="user"
            name="basic"
            :label-col="{ span: 4 }"
            :wrapper-col="{ span: 18 }"
            autocomplete="on"
        >
          <!--:rules="[{ required: true, message: 'Please input your username!' }]"-->
          <a-form-item label=用户名>
            <a-input v-model:value="user.loginName" :disabled="!!user.id"/>
          </a-form-item>

          <a-form-item label=昵称>
            <a-input v-model:value="user.name"/>
          </a-form-item>

          <a-form-item label=密码>
            <a-input v-model:value="user.password"/>
          </a-form-item>

        </a-form>
      </a-modal>
    </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
import {createVNode, defineComponent, onMounted, ref} from 'vue';
import axios from 'axios';
import {message, Modal} from "ant-design-vue";
import {ExclamationCircleOutlined} from "@ant-design/icons-vue";
import {Tool} from "@/util/tool";
//告诉编译器这两个已经存在与全局引用中了
declare let hexMd5:any;
declare let KEY:any;
export default defineComponent({
  name: 'AdminUser',
  setup() {
    const user = ref();
    const users = ref();
    const keyword = ref();
    const pagination = ref({
      current: 1,
      pageSize: 8,
      total: 0
    });
    const loading = ref(false);
    const columns = [

      {
        title: '用户名',
        dataIndex: 'loginName',
      },
      {
        title: '昵称',
        dataIndex: 'name',
      },
      {
        title: '密码',
        dataIndex: 'password',
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

    const add = () => {
      open.value = true;
      user.value = {};
    }
    const edit = (record:any) => {
      open.value = true;
      user.value = Tool.copy(record);
    }
    const search = ()=> {
        handleQuery(
            {
              loginName:keyword.value,
              page: 1,
              size: pagination.value.pageSize
            }
        )
    }

    const handleOk = () => {
      confirmLoading.value = true;
      user.value.password = hexMd5(user.value.password + KEY);
      axios.post("/user/save", {
        id:user.value.id,
        loginName:user.value.loginName,
        name:user.value.name,
        password: user.value.password
      }).then(
          (response) => {
            const data = response.data;
            confirmLoading.value = false;
            if (data.success) {
              open.value = false;
              //重新加载页面
              handleQuery({
                page: pagination.value.current,
                size: pagination.value.pageSize
              });
            } else {
              confirmLoading.value = false;
              message.error(data.message);
            }
          }
      )
    }

    // 数据查询
    const handleQuery = (params: any) => {
      loading.value = true;
      axios.get("/user/list", {
        params:params
      }).then((response) => {
        loading.value = false;
        const data = response.data;
        users.value = data.content.list;
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

    const showDeleteConfirm = (id:number,loginName:string) => {
      Modal.confirm({
        title: 'Are you sure delete this user?',
        icon: createVNode(ExclamationCircleOutlined),
        content: loginName,
        okText: 'Yes',
        okType: 'danger',
        cancelText: 'No',
        onOk() {
          DeleteUser(id);
        },
      });
    };

    const DeleteUser = (id: number) => {
      axios.delete("/user/delete/" + id).then((response) => {
        const data = response.data;
        if (data.success) {
          //重新加载页面
          handleQuery({
            page: 1,
            size: pagination.value.pageSize
          });
        }
        else {
          message.error("错误！！")
        }
      })
    }

    onMounted(() => {
      handleQuery({
        page: pagination.value.current,
        size: pagination.value.pageSize
      });
    })

    return {
      users,
      pagination,
      columns,
      loading,
      confirmLoading,
      open,
      keyword,
      user,
      add,
      edit,
      handleOk,
      handleTableChange,
      search,
      showDeleteConfirm
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