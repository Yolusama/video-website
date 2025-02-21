<template>
   <div id="login">
    <el-tabs v-model="state.activeTabName" type="card" @tab-change="clearInput">
    <el-tab-pane label="电子邮箱+密码登录" name="emailNPwd">
        <el-form label-width="auto" class="login">
        <el-input type="text" v-model="state.email" placeholder="电子邮箱" :prefix-icon="User" maxlength="30"></el-input>
        <el-input v-model="state.password" placeholder="密码" :prefix-icon="Lock" maxlength="20" minlength="6" type="password"></el-input>
        <div><el-button type="primary" plain @click="login" class="login-btn">登录</el-button>
        <el-button type="info" plain @click="state.activeTabName='emailCheck'">注册</el-button></div>
     </el-form>
    </el-tab-pane>
    <el-tab-pane label="电子邮箱验证码登录" name="emailCheck" >
         <el-form label-width="auto" class="login">
        <el-input type="text" v-model="state.email" placeholder="电子邮箱" :prefix-icon="User" maxlength="30" style="width:360px">
            <template #append>
            <el-button @click="askForCheckCode" :disabled="state.hasGotCode">{{state.checkText}}</el-button>
            </template>
        </el-input>
        <el-input v-model="state.checkCode" :prefix-icon="Promotion" :maxlength="state.checkCodeLength" style="width:360px"></el-input>
        <div>
        <el-button type="primary" plain @click="indentifyLogin" class="login-btn">登录</el-button>
        <el-button type="info" plain @click="register">注册</el-button>
    </div>
     </el-form>
    </el-tab-pane>
  </el-tabs>
     
   </div>
</template>

<script setup>
import {reactive,onMounted} from "vue";
import { Lock,User,Promotion } from "@element-plus/icons-vue"
import { AskForCheckCode, IdentifyLogin, Login, Register } from "@/api/User";
import stateStroge from "@/modules/StateStorage";
import { authorization, defalutLodingColor, formDataAuth, LoadingOperate } from "@/modules/Common";
import { freshPage } from "@/modules/domHelper";

const state = reactive({
    email:"",
    password:"",
    checkCode:"",
    checkText:"获取验证码",
    activeTabName:"emailNPwd",
    expire:1000,
    hasGotCode:false,
    checkCodeLength:6
});

onMounted(()=>{
   
});

async function login()
{
   const res = await Login(state.email,state.password);
   if(res==null)return;
   afterLogining(res);
}

async function indentifyLogin() {
    const res = await IdentifyLogin(state.email,state.checkCode);
    if(res==null)return;
    afterLogining(res);
}

async function askForCheckCode()
{
    var i = 60;
    state.hasGotCode = true;
    const timer = setInterval(()=>{
         state.checkText = `${--i}s`;
         if(i==0)
         {
            state.hasGotCode = false;
            state.checkText = "获取验证码";
            clearInterval(timer);
         }
    },state.expire);
    await AskForCheckCode(state.email,state.checkCodeLength);
}

async function register() {
    const res = await Register(state.checkCode,state.email);
    if(!res)return;
    freshPage();
}

function clearInput()
{
    state.email = "";
    state.checkCode = "";
    state.password = "";
}

function afterLogining(data)
{
   stateStroge.set("uid",data.id);
   stateStroge.set("email",data.mail);
   stateStroge.set("name",data.name);
   stateStroge.set("avatar",data.avatar);
   stateStroge.set("type",data.type);
   stateStroge.set("token",data.token);
   authorization.headers.token = data.token;
   formDataAuth.headers.token = data.token;
   LoadingOperate(true,"登录中...",defalutLodingColor,()=>{
         state.hasLogan = true;
         freshPage();
   },2000);
}


</script>

<style scoped>
#login
{
   position: relative;
   height: 360px;
   width: 100%;
   display: flex;
   align-items: center;
   justify-content: center;
}

#login .login
{
    position: relative;
    height: 300px;
    width: 500px;
    display: flex;
    flex-flow: column nowrap;
    justify-content: center;
    align-items: center;
    background-color: aliceblue;
    padding: 1%;
    border-radius: 5px;
    margin-left: 3px;
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