<template>
  <a-layout id="the-header-style">
    <a-layout-header class="header">
      <div class="logo"/>
      <a :style="{float:'right', color:'#bbb'}" >
        <div class="loginLogo" v-if="!userShow.id" @click="login">登录</div>
        <div class="loginLogo" v-else>{{userShow.name}}</div>
      </a>
      <a-menu
          theme="dark"
          mode="horizontal"
          :style="{ lineHeight: '64px' }"
      >
        <a-menu-item key="/">
          <router-link to="/">首页</router-link>
        </a-menu-item>
        <a-menu-item key="/admin/articles">
          <router-link to="/admin/articles">电子书管理</router-link>
        </a-menu-item>
        <a-menu-item key="/admin/category">
          <router-link to="/admin/category">分类管理</router-link>
        </a-menu-item>
        <a-menu-item key="/admin/user">
          <router-link to="/admin/user">用户管理</router-link>
        </a-menu-item>
        <a-menu-item key="/about">
          <router-link to="/about">关于我们</router-link>
        </a-menu-item>

      </a-menu>

      <a-modal
          v-model:open="loginOpen"
          title="登录"
          :confirm-loading="loginConfirmLoading"
          okText="Login"
          @ok="handleLoginOk">
        <a-form
            :model="user"
            name="basic"
            :label-col="{ span: 4 }"
            :wrapper-col="{ span: 18 }"
            autocomplete="on"
        >
          <a-form-item label="用户名" :style="{marginTop:'20px'}">
            <a-input v-model:value="user.loginName">
              <template #prefix>
                <user-outlined/>
              </template>
            </a-input>
          </a-form-item>

          <a-form-item label="密码" :style="{marginTop:'20px'}">
            <a-input-password v-model:value="user.password">
              <template #prefix>
                <lock-outlined/>
              </template>
            </a-input-password>

          </a-form-item>

        </a-form>
      </a-modal>
    </a-layout-header>
  </a-layout>
</template>

<script lang="ts">
import {defineComponent, ref} from 'vue';
import {UserOutlined, LockOutlined} from "@ant-design/icons-vue"
import {message} from "ant-design-vue";
import {Tool} from "@/util/tool";
import axios from "axios";
declare let hexMd5: any;
declare let KEY: any;

export default defineComponent({
  name: 'the-header',
  components:{
    UserOutlined,
    LockOutlined,
  },
  setup() {
    const loginOpen = ref(false);
    const loginConfirmLoading = ref(false);
    const user = ref();
    const userShow = ref();
    userShow.value ={};
    const isLogin = ref(false);
    const handleLoginOk = () => {
      console.log("login");
      loginConfirmLoading.value = true;
      const loginUser:any = {};
      loginUser.value = Tool.copy(user.value);
      loginUser.value.password = hexMd5(loginUser.value.password + KEY);
      axios.post("/user/login", {
        loginName: loginUser.value.loginName,
        password: loginUser.value.password
      }).then((response)=>{
        const data = response.data;
        console.log(data);
        loginConfirmLoading.value = false;
        if(data.success){
          loginOpen.value = false;
          message.success("登录成功");
          userShow.value =data.content;
        }
        else {
          message.error(data.message);
        }
      })

    }
    const login = () => {
      user.value = {
        loginName:'sarfa',
        password:'root1234'
      };
      loginOpen.value = true;
    }

    return {
      loginOpen,
      loginConfirmLoading,
      user,
      userShow,
      isLogin,
      handleLoginOk,
      login


      , UserOutlined
      , LockOutlined

    }
  }
});
</script>

<style scoped>
#the-header-style .logo {
  float: left;
  width: 120px;
  height: 31px;
  margin: 16px 24px 16px 0;
  background: rgba(255, 255, 255, 0.3);
}

.ant-row-rtl #cthe-header-style .logo {
  float: right;
  margin: 16px 0 16px 24px;
}
.loginLogo{
  width: 44px;
  height: 44px;
  line-height: 44px;
  margin-top: 10px;
  margin-bottom: 10px;
  border-radius: 50%;
  text-align: center;
  background: rgba(255, 255, 255, 0.1);
}
/*
.loginLogo:hover{
  transform: translateY(-2px);
  box-shadow: 0 15px 30px rgba(255, 255, 255, 2);
}*/
</style>
