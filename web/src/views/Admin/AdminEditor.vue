<template>
  <a-layout :style="{padding: '0 50px',minHeight: '280px'}">
    <a-row style="margin: 10px 0">
      <a-col :span="22">
        <a-input v-model:value="articleName" placeholder="请输入文章标题" ></a-input>
      </a-col>
      <a-col :span="2">
        <a-button  :style="{float: 'right',width:'120px'}" @click="edit">{{buttonStatus}}</a-button>
      </a-col>
    </a-row>
    <md-editor v-model="text" class="AdminEditor"></md-editor>
    <a-modal v-model:open="open" title="电子书" :confirm-loading="confirmLoading" @ok="handleOk">
      <a-form
          :model="articles"
          name="basic"
          :label-col="{ span: 4 }"
          :wrapper-col="{ span: 18 }"
          autocomplete="on"
      >
        <!--      :rules="[{ required: true, message: 'Please input your username!' }]"-->
        <a-form-item label=封面>
          <a-input v-model:value="articles.cover"/>
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
  </a-layout>
</template>

<script lang="ts">
import {defineComponent, ref, onMounted,} from 'vue';
import {useRoute} from 'vue-router'
import {MdEditor} from "md-editor-v3";
import "md-editor-v3/lib/style.css";
import axios from "axios";
import {message} from "ant-design-vue";
import {Tool} from "@/util/tool";
import router from "@/router";

export default defineComponent({
  name: 'AdminEditor',
  components: {
    MdEditor,
  },
  setup() {
    const text = ref();
    const articles = ref();
    const open = ref<boolean>(false);
    const confirmLoading = ref<boolean>(false);
    const categoryId = ref();
    const listCategory = ref();
    let categorys:any;
    const route = useRoute();
    const articleId = ref();
    const articleName = ref();
    const buttonStatus =ref();

    const edit = () => {
      open.value = true;
      if(Tool.isNotEmpty(articleId.value)){
        categoryId.value = [getCategoryName(articles.value.category1Id),getCategoryName(articles.value.category2Id)];
        if(categoryId.value[0] === ''){
          categoryId.value = [];
        }
      }
      else {
        articles.value = {};
        categoryId.value = [];
      }

    }

    const handleOk = () => {
      confirmLoading.value = true;
      articles.value.category1Id = getCategoryId(categoryId.value[0]);
      articles.value.category2Id = getCategoryId(categoryId.value[1]);
      articles.value.name = articleName.value;
      articles.value.content = text.value;
      axios.post("/articles/save", articles.value).then(
          (response) => {
            const data = response.data;
            if (data.success) {
              open.value = false;
              confirmLoading.value = false;
              //成功后0.5秒回到电子书管理列表
              message.success("修改成功");
              setTimeout(()=>{
                router.push("/admin/articles");
              },500);


            } else {
              confirmLoading.value = false;
              message.error(data.message);
            }
          }
      )

    };

    const handleQueryArticles = () => {
      axios.get("/articles/list", {
        params: {
          id: articleId.value
        }
      }).then(
          (response) => {
            const data = response.data;
            if (data.success) {
              articles.value = Tool.copy(data.content.list[0]);
              articleName.value = articles.value.name;
              console.log(articles.value.name);
            }
          })

    }
    const handleQueryCategory = () => {
      axios.get("/category/list").then(
          (response) => {
            const data = response.data;
            categorys = data.content.list;
            listCategory.value = [];
            listCategory.value = Tool.array2Tree(categorys,0);

          })
    }

    const handleQueryContent = () => {
          axios.get("/articles/findContent/" + articleId.value).then((response)=>{
            const data = response.data
            if(data.success){
              text.value = data.content;
            }
          })
    }

    const getCategoryName = (cid:number) => {
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
    onMounted(() => {
      if(Tool.isNotEmpty(route.query)){
        buttonStatus.value = "修改文章"
        articleId.value = route.query.articlesId;
        handleQueryArticles();
        handleQueryContent();
      }
      else {
        buttonStatus.value = "发布文章"
      }
      handleQueryCategory();
    })

    return {
      text,
      articles,
      open,
      confirmLoading,
      categoryId,
      listCategory,
      articleName,
      buttonStatus,
      handleOk,
      edit

    }
  }
});
</script>

<style scoped>
.AdminEditor {
  height: 700px;

}
</style>