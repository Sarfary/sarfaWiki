<template>
  <a-layout :style="{padding: '0 210px',minHeight: '280px'}">
    <a-row>
      <a-col :span="20">
        <div class="Preview">
          <MdPreview :editorId="id" :modelValue="text"/>
        </div>
      </a-col>

      <a-col :span="4">
        <div class="Catalog">
          <MdCatalog :editorId="id" :scrollElement="scrollElement"/>
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
    const scrollElement = document.documentElement;
    const route = useRoute();
    const articleId = ref();
    const handleQueryContent = () => {
      axios.get("/articles/findContent/" + articleId.value).then((response) => {
        const data = response.data
        if (data.success) {
          text.value = data.content;
        }
      })
    }

    onMounted(() => {
      articleId.value = route.query.articleId;
      handleQueryContent();
    })
    return {
      scrollElement,
      text,
      id
    }
  }
})
</script>

<style scoped>

.Catalog {
  margin-left: 10px;
  background: #fff;
  position: fixed;
  width: 200px;
  //height: 100%;
  border-radius: 10px;
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.05);
}

.Preview{
  border-radius: 10px;
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.05);
}
</style>