<template>
  <a-layout :style="{padding: '0 50px',minHeight: '280px'}">
    <a-layout-content style="padding: 24px 0; background: #fff">
      <div class="addAndSearch">
        <a-button type="primary" @click="add" style="margin-left:20px ">
          新增
        </a-button>


      </div>

      <a-table
          :columns="columns"
          :data-source="listDoc"
          row-key="id"
          :pagination="false"
          :loading="loading"
          @change="handleTableChange"
      >
        <template v-slot:action="{ text, record }">
          <a-space size="large">
            <a-button type="primary" @click="edit(record)">编辑</a-button>
            <a-popconfirm
                title="Are you sure delete this doc?"
                ok-text="Yes"
                cancel-text="No"
                @confirm="DeleteDoc(record.id)"
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
        :model="doc"
        name="basic"
        :label-col="{ span: 4 }"
        :wrapper-col="{ span: 18 }"
        autocomplete="on"
    >
      <!--:rules="[{ required: true, message: 'Please input your username!' }]"-->
      <a-form-item label=名称>
        <a-input v-model:value="doc.name"/>
      </a-form-item>

      <a-form-item label=父分类>
<!--        <a-input v-model:value="doc.parent"/>-->
        <a-select
            ref="select"
            v-model:value="doc.parent"
            placeholder=""
        >
          <a-select-option value="0">
            无
          </a-select-option>
          <a-select-option v-for="c in listDoc" :key="c.id" :value="c.id" :disabled="doc.id == c.id">
            {{c.name}}
          </a-select-option>
        </a-select>
      </a-form-item>

      <a-form-item label=顺序>
        <a-input v-model:value="doc.sort"/>
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
  name: 'AdminDoc',
  setup() {
    const docs = ref();
    const doc = ref();
    const loading = ref(false);
    const listDoc = ref();
    const columns = [

      {
        title: '名称',
        dataIndex: 'name',
        width:'400px'
      },
      {
        title: '父文档',
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
      doc.value = Tool.copy(record);
    };
    const add = () => {
      open.value = true;
      doc.value = {};
    }

    // 数据查询
    const handleQuery = (params: any) => {
      loading.value = true;
      axios.get("/doc/list", {
        params:params
      }).then((response) => {
        loading.value = false;
        const data = response.data;
        docs.value = data.content.list;
        listDoc.value = [];
        listDoc.value = Tool.array2Tree(docs.value,0);
        listDoc.value = Tool.parentIdToParentName(listDoc.value,0,'无');
      });
    };
    const handleTableChange = () => {
      handleQuery({
      });
    };
    const handleOk = () => {
      confirmLoading.value = true;
      doc.value = Tool.parentNameToParentId(doc.value,listDoc.value);
      axios.post("/doc/save", {
        id:doc.value.id,
        parent:doc.value.parent,
        name:doc.value.name,
        sort:doc.value.sort
      }).then(
          (response) => {
            const data = response.data;
            confirmLoading.value = false;
            if (data.success) {
              open.value = false;
              //重新加载页面
              handleQuery({});
            } else {
              confirmLoading.value = false;
              message.error(data.message);
            }
          }
      )

    };
    const DeleteDoc = (id: number) => {
      axios.delete("/doc/delete/" + id).then((response) => {
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
      docs,
      listDoc,
      columns,
      loading,
      confirmLoading,
      open,
      doc,
      handleTableChange,
      edit,
      add,
      handleOk,
      DeleteDoc


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