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
        <a-tree-select
            v-model:value="doc.parent"
            show-search
            style="width: 100%"
            :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
            allow-clear
            tree-default-expand-all
            :tree-data="treeSelectData"
            :replaceFields="{label: 'name', value: 'id'}"
        >
        </a-tree-select>
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
import { useRoute } from 'vue-router';

export default defineComponent({
  name: 'AdminDoc',
  setup() {
    const route = useRoute();
    const docs = ref();
    const doc = ref();
    const loading = ref(false);
    const listDoc = ref();
    const firstList = ref();
    const treeSelectData = ref();
    treeSelectData.value = [];
    const columns = [
      {
        title: '名称',
        dataIndex: 'name',
        width:'240px'
      },
      {
        title: '父文档',
        dataIndex: 'parent',
        width:'240px'
      },
      {
        title: '顺序',
        dataIndex: 'sort',
        width:'240px'
      },
      {
        title: '阅读数',
        dataIndex:'viewCount',
        width:'240px'
      },
      {
        title: '点赞数',
        dataIndex:'voteCount',
        width:'240px'
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
      treeSelectData.value = Tool.copy(firstList.value);
      setDisabled(treeSelectData.value,record.id);
      treeSelectData.value.unshift({id: 0, name: '无'});
      console.log(treeSelectData.value);
    };
    const add = () => {
      open.value = true;
      doc.value = {};
      treeSelectData.value = Tool.copy(firstList.value);
      treeSelectData.value.unshift({id: 0, name: '无'});

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
        firstList.value = Tool.copy(listDoc.value);
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
      axios.post("/doc/save", doc.value
      ).then(
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
    const setDisabled = (treeSelectData:any,id:number) => {
        for(let i=0;i<treeSelectData.length;i++){
          const node = treeSelectData[i];
          if(Number(node.id) === Number(id)){
            node.disabled = true;
            if(Tool.isNotEmpty(node.children)){
              for (let j=0;j<node.children.length;j++){
                setDisabled(node.children,node.children[j].id);
              }
            }
          }
          else {
            if(Tool.isNotEmpty(node.children)){
              setDisabled(node.children,id);
            }
          }
        }
    }
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
      treeSelectData,
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