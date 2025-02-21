<template>
  <div id="home">
    <el-menu :router="true" mode="vertical" 
           :default-active="route.path" 
           class="main-menu"
        active-text-color="#ffd04b"
        background-color="#545c64"
        text-color="white"
        >
        <el-menu-item :index="state.routes['user']">
           <el-icon><Avatar /></el-icon>用户管理
        </el-menu-item>
        <el-sub-menu index="/Home/Video">
           <template #title>
            <el-icon><Film /></el-icon>视频管理功能
           </template>
           <el-menu-item :index="state.routes['video']">
           <el-icon><Memo /></el-icon> 视频管理
           </el-menu-item>
           <el-menu-item :index="state.routes['videoUpload']">
           <el-icon><Upload /></el-icon> 上传视频
           </el-menu-item>
          
        </el-sub-menu>
         <el-menu-item :index="state.routes['userInfo']">
           <el-icon><User></User></el-icon>个人信息
        </el-menu-item>
        <el-menu-item @click="logout">
            <el-icon><Operation /></el-icon>退出登录
        </el-menu-item>
    </el-menu>
    <div class="content">
         <router-view></router-view>
    </div>
  </div>
</template>
<script setup>
import { BeforeRouteLeave } from "@/modules/Common";
import { reactive,onMounted } from "vue";
import { onBeforeRouteLeave, useRoute } from "vue-router";

const route = useRoute();
const state = reactive({
    routes:{},
    hasLogouted:false
});

onMounted(function(){
    state.routes['user'] = "/Home/UserManage";
    state.routes['video'] = "/Home/Video/Manage";
    state.routes['videoUpload'] = "/Home/Video/Upload";
    state.routes['userInfo'] = "/Home/UserInfo";
});



onBeforeRouteLeave(function(to,from,next){
   BeforeRouteLeave(to,from,next,state.hasLogouted);
});

</script>
<style scoped>
  #home
  {
    position: relative;
    width: 100%;
    display: flex;
  }
  #home .main-menu
  {
    width: 220px;
    height: 660px;
  }
</style>