import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import AdminArticles from "@/views/Admin/AdminArticles.vue";
import AdminCategory from "@/views/Admin/AdminCategory.vue";
import AdminUser from "@/views/Admin/AdminUser.vue";
import ArticleView from "@/views/ArticleView.vue";
import AdminEditor from "@/views/Admin/AdminEditor.vue";
import store from "@/store";
import {Tool} from "@/util/tool";
import {message} from "ant-design-vue";
const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/about',
    name: 'about',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
  },
  {
    path: '/admin/articles',
    name: 'adminArticles',
    component: AdminArticles,
    meta:{
      loginRequire: true
    }
  },
  {
    path: '/admin/category',
    name: 'adminCategory',
    component:AdminCategory,
    meta:{
      loginRequire: true
    }
  },
  {
    path: '/admin/user',
    name: 'adminUser',
    component:AdminUser,
    meta:{
      loginRequire: true
    }
  },
  {
    path: '/admin/editor',
    name: 'adminEditor',
    component:AdminEditor,
    meta:{
      loginRequire: true
    }
  },
  {
    path: '/articleView',
    name: 'articleView',
    component:ArticleView
  },


]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})
// 路由登录拦截
router.beforeEach((to, from, next) => {
  // 要不要对meta.loginRequire属性做监控拦截
  if (to.matched.some(function (item) {
    console.log(item, "是否需要登录校验：", item.meta.loginRequire);
    return item.meta.loginRequire
  })) {
    const loginUser = store.state.user;
    if (Tool.isEmpty(loginUser)) {
      console.log("用户未登录！");
      message.error("用户未登录！");
      next('/');
    } else {
      next();
    }
  } else {
    next();
  }
});

export default router
