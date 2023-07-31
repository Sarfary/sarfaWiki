<template>
  <a-layout :style="{padding: '0 50px',minHeight: '280px'}">
    <a-breadcrumb style="margin: 16px 0">
      <a-breadcrumb-item>
        <a href="/">首页</a>
      </a-breadcrumb-item>
      <a-breadcrumb-item>
        <router-link to="/admin/ebooks">电子书管理</router-link>
      </a-breadcrumb-item>
      <a-breadcrumb-item>
        <router-link :to="'/admin/doc?ebookId=' + ebookId">文档管理</router-link>
      </a-breadcrumb-item>
    </a-breadcrumb>
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
            <router-link to="/admin/editor">
              <a-button type="primary">编辑文档</a-button>
            </router-link>
            <a-button type="primary" @click="edit(record)">编辑</a-button>
            <a-popconfirm
                title="Are you sure delete the documents?"
                ok-text="Yes"
                cancel-text="No"
                @confirm="showDeleteConfirm(record.id)"
            >
              <a-button type="primary" danger block>删除</a-button>

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
            :fieldNames="{label: 'name', value: 'id'}"
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
import {defineComponent, onMounted, ref, createVNode} from 'vue';
import axios from 'axios';
import {message} from "ant-design-vue";
import {Tool} from "@/util/tool";
import { useRoute } from 'vue-router';
import { Modal } from 'ant-design-vue';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';



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
    const ebookId = route.query.ebookId;
    const text = ref();
    treeSelectData.value = [];
    const columns = [
      {
        title: '名称',
        dataIndex: 'name',
        width:'300px'
      },
      {
        title: '顺序',
        dataIndex: 'sort',
        width:'300px'
      },
      {
        title: '阅读数',
        dataIndex:'viewCount',
        width:'300px'
      },
      {
        title: '点赞数',
        dataIndex:'voteCount',
        width:'300px'
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
      treeSelectData.value = Tool.copy(firstList.value)||[];
      setDisabled(treeSelectData.value,record.id);
      treeSelectData.value.unshift({id: 0, name: '无'});
    };
    const add = () => {
      open.value = true;
      doc.value = {
        ebookId:route.query.ebookId
      };
      treeSelectData.value = Tool.copy(firstList.value)||[];
      treeSelectData.value.unshift({id: 0, name: '无'});

    }

    // 数据查询
    const handleQuery = () => {
      loading.value = true;
      axios.get("/doc/list", {
        params:{
          ebookId:route.query.ebookId
        }
      }).then((response) => {
        loading.value = false;
        const data = response.data;
        docs.value = data.content.list;
        listDoc.value = [];
        listDoc.value = Tool.array2Tree(docs.value,0);
        firstList.value = Tool.copy(listDoc.value);
        treeSelectData.value = Tool.copy(firstList.value)||[];
        console.log(treeSelectData.value);
        // listDoc.value = Tool.parentIdToParentName(listDoc.value,0,'无');

      });
    };
    const handleTableChange = () => {
      handleQuery();
    };
    const handleOk = () => {
      confirmLoading.value = true;
      // doc.value = Tool.parentNameToParentId(doc.value,listDoc.value);
      axios.post("/doc/save", doc.value
      ).then(
          (response) => {
            const data = response.data;
            confirmLoading.value = false;
            if (data.success) {
              open.value = false;
              //重新加载页面
              handleQuery();
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
    const deleteIdList:Array<string> = [];
    const deleteNameList:Array<string> = [];
    const getDeleteIdList = (id: number,treeSelectData:any) => {
      for (let i=0;i<treeSelectData.length;i++){
        const node = treeSelectData[i];
        if(Number(node.id) === Number(id)){
          deleteIdList.push(String(id));
          deleteNameList.push(node.name);
          if(Tool.isNotEmpty(node.children)){
            for (let j=0;j<node.children.length;j++){
              getDeleteIdList(node.children[j].id,node.children);
            }
          }
        }
        else {
          if(Tool.isNotEmpty(node.children)){
            getDeleteIdList(id,node.children);
          }
        }

      }

    }

    const showDeleteConfirm = (id:number) => {
      //初始化
      deleteIdList.splice(0,deleteIdList.length);
      deleteNameList.splice(0,deleteNameList.length);
      //得到所有的要删除的文档
      getDeleteIdList(id,treeSelectData.value);
      Modal.confirm({
        title: 'Are you sure delete the following documents?',
        icon: createVNode(ExclamationCircleOutlined),
        content: deleteNameList.join(","),
        okText: 'Yes',
        okType: 'danger',
        cancelText: 'No',
        onOk() {
          handleDelete();
        },
      });
    };

    const handleDelete = () => {
      axios.delete("/doc/delete/"+ deleteIdList.join(",")).then((response) => {
        const data = response.data;
        if (data.success) {
          //重新加载页面
          handleQuery();
        }
      })
    }

    onMounted(() => {
      handleQuery();
    })

    return {
      ebookId,
      docs,
      listDoc,
      columns,
      loading,
      confirmLoading,
      open,
      doc,
      treeSelectData,
      text,
      handleTableChange,
      edit,
      add,
      handleOk,
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