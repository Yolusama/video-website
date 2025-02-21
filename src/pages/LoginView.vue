<template>
   <div id="login">
    <img src="../imgs/intro_back.png" alt="" style="width:420px;height:430px">
     <el-form label-width="auto" class="login">
        <el-input type="text" v-model="state.account" placeholder="电子邮箱/用户id" :prefix-icon="User" maxlength="30"></el-input>
        <el-input v-model="state.password" placeholder="密码" :prefix-icon="Lock" maxlength="20" minlength="6" type="password"></el-input>
        <el-button type="primary" plain @click="login" class="login-btn">登录</el-button>
     </el-form>
   </div>
</template>

<script setup>
import {reactive} from "vue";
import { Lock,User } from "@element-plus/icons-vue"
import { Login } from "@/api/User";
import { ElMessage } from "element-plus";
import stateStroge from "@/modules/StateStorage";
import { authorization, BeforeRouteLeave, defalutLodingColor, formDataAuth, LoadingOperate } from "@/modules/Common";
import { switchPage } from "@/modules/domHelper";
import { onBeforeRouteLeave } from "vue-router";

const state = reactive({
    account:"",
    password:"",
    hasLogan:false
});

async function login()
{
   const res = await Login(state.account,state.password);
   if(res==null)return;
   if(res.type!=4){
    ElMessage({
        message:"非管理员！",
        type:"error"
    });
    return;
   }
   stateStroge.set("uid",res.id);
   stateStroge.set("email",res.mail);
   stateStroge.set("name",res.name);
   stateStroge.set("avatar",res.avatar);
   stateStroge.set("type",res.type);
   stateStroge.set("token",res.token);
   authorization.headers.token = res.token;
   formDataAuth.headers.token = res.token;
   LoadingOperate(true,"登录中...",defalutLodingColor,()=>{
         state.hasLogan = true;
         switchPage("#/Home");
   },2000);
}

onBeforeRouteLeave((to,from,next)=>{
   BeforeRouteLeave(to,from,next,state.hasLogan);
});

</script>

<style scoped>
#login
{
   position: relative;
   height: 650px;
   width: 100%;
   display: flex;
   align-items: center;
   justify-content: center;
}

#login .login
{
    position: relative;
    height: 430px;
    width: 350px;
    display: flex;
    flex-flow: column nowrap;
    justify-content: center;
    background-color: aliceblue;
    padding: 1%;
    border-radius: 5px;
}


#login .login .el-input
{
    width: 300px;
    margin-bottom: 13px;
}

#login .login-btn
{
    margin: 0 auto;
    width: 120px;
}

</style>