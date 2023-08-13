import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import Antd from 'ant-design-vue'
import 'ant-design-vue/dist/reset.css'
import axios from "axios";
import {Tool} from "@/util/tool";
axios.defaults.baseURL =process.env.VUE_APP_SERVER;
/**
 * axios拦截器
 */
axios.interceptors.request.use(function (config) {
    console.log('请求参数：', config);
    const token = store.state.user.token;
    if(Tool.isNotEmpty(token)){
        config.headers.token = token;
        console.log("request的headers增加token：",token);
    }
    return config;
}, error => {
    return Promise.reject(error);
});
axios.interceptors.response.use(function (response) {
    console.log('返回结果：', response);
    return response;
}, error => {
    console.log('返回错误：', error);
    return Promise.reject(error);
});


createApp(App).use(store).use(router).use(Antd).mount('#app')

console.log(process.env.NODE_ENV)
console.log(process.env.VUE_APP_SERVER)