<template>
  <a-layout :style="{padding: '0 210px',minHeight: '280px'}">
    <a-row>
      <a-col :span="20">
        <div>
          <div class="Preview">
            <h2 :style="{paddingTop: '20px',paddingLeft:'20px'}">{{articleName}}</h2>
            <div :style="{paddingTop: '10px',paddingLeft:'20px'}">
              <a-space :size="20">
                <h1>阅读数:{{viewCount}}</h1>
                <h1>点赞数:{{voteCount}}</h1>
              </a-space>
            </div>
            <a-divider style="height: 4px; background-color: #9999cc"/>
          </div>
          <MdPreview :editorId="id" :modelValue="text"  showCodeRowNumber="true"/>
        </div>
      </a-col>

      <a-col :span="4">
        <div class="Catalog">
          <MdCatalog
              :editorId="id"
              :scrollElement="scrollElement"
              :scrollElementOffsetTop = 100
              :style="{fontsize: '15px'}"/>
        </div>
      </a-col>
    </a-row>
  </a-layout>


</template>

<script lang="ts">
import {defineComponent, onMounted} from "vue";
import {ref} from 'vue';
import {MdPreview, MdCatalog} from 'md-editor-v3';
// preview.css相比style.css少了编辑器那部分样式
import 'md-editor-v3/lib/preview.css';
import 'md-editor-v3/lib/style.css';
import axios from "axios";
import {useRoute} from 'vue-router'

export default defineComponent({
  name: 'articleView',
  components: {
    MdPreview,
    MdCatalog
  },
  setup() {
    const id = 'preview-only';
    const text = ref();
    const articleName =ref();
    const viewCount = ref();
    const voteCount = ref();
    const scrollElement = document.documentElement;
    const route = useRoute();
    const articleId = ref();
    const handleQueryContentAndArticle = () => {
      axios.get("/articles/findContent/" + articleId.value).then((response) => {
        const data = response.data
        if (data.success) {
          text.value = data.content;
          handleQueryArticle();
        }
      })
    }
    const handleQueryArticle = () => {
      axios.get("/articles/findArticle/" + articleId.value).then((response) => {
        const data = response.data
        if (data.success) {
          const article =ref();
          article.value = data.content;
          articleName.value = article.value.name;
          viewCount.value = article.value.viewCount;
          voteCount.value = article.value.voteCount;
        }
      })
    }
    onMounted(() => {
      articleId.value = route.query.articleId;
      handleQueryContentAndArticle();
    })
    return {
      scrollElement,
      text,
      id,
      articleName,
      viewCount,
      voteCount
    }
  }
})
</script>

<style scoped>

.Catalog {
  margin-left: 10px;
  background: #fff;
  position: fixed;
  width: 250px;
  //height: 100%;
}

.Preview{
  //margin-bottom: 10px;
  background: #fff;
  height: 120px;
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.05);
}
</style>