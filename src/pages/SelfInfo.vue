<template>
   <div id="self-info">
    <h4>uid：{{user.id}}</h4>
    <h4>电子邮箱：{{user.email}}</h4>
    <div class="info">
        <el-image :src="imgSrc(user.avatar)" :preview-src-list="[imgSrc(user.avatar)]" :preview-teleported="true"></el-image>
        <span>{{user.name}}</span>
        <span>账号状态：
         <el-tag type="success" v-if="user.status==1">正常</el-tag>
         <el-tag tpye="danger" v-else>异常</el-tag>
     </span>
    </div>
    <div class="time">注册时间：{{new Date(user.createTime).toLocaleString()}}</div>
    <div class="time">上次登录：{{new Date(user.lastLoginTime).toLocaleString()}}</div>
     <div class="type">用户类型：{{getUserType(user.type)}}</div>
    <el-button type="primary" plain @click="state.pwdChangeDialogShow=true">修改密码</el-button>
   </div>
   <el-dialog v-model="state.pwdChangeDialogShow" @close="clear">
    <el-from></el-from>
       <el-input type="email" v-model="user.email" :prefix-icon="Message" :readonly="true">
           <template #append>
                <el-button :disabled="state.hasGotCode" @click="askForCheckCode">{{state.checkCodeText}}</el-button>
           </template>
       </el-input>
       <el-input type="password" v-model="state.newPassword" @input="checkPwd" show-password :prefix-icon="Lock" placeholder="新密码"></el-input>
       <el-input v-model="state.checkCode" :prefix-icon="Promotion" maxlength="5"></el-input>
       <el-button type="success" plain @click="changePassword">确定</el-button>
       <el-button type="info" plain @click="state.pwdChangeDialogShow=false">取消</el-button>
   </el-dialog>
</template>

<script setup>
import { ref,reactive,onMounted } from "vue";
import { imgSrc } from "@/modules/AxiosHelper";
import { Message,Promotion,Lock } from "@element-plus/icons-vue"
import { GetUser, getUserType } from "@/api/User";
import stateStroge from "@/modules/StateStorage";
const state = reactive({
     pwdChangeDialogShow:false,
     checkCodeText:"获取验证码",
     hasGotCode:false,
     checkCode:"",
     newPassword:""
});

const user = ref({});

onMounted(async ()=>{
    const res = await GetUser(stateStroge.get("uid"));
    if(res==null)return;
    user.value = res;
});

function checkPwd()
{
    const value = state.newPassword;
    state.newPassword = value.replace(/\s/,"");
}

function clear()
{
    state.e
}

</script>

<style>

</style>