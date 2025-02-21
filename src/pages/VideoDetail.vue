<template>
    <div id="video-detail" v-if="state.video!=null">
        <h3 class="title">{{state.video.title}}</h3>
         <div class="user">
            <el-icon class="download" v-if="state.hasLogan" @click="download">
                <Download></Download>
            </el-icon>
      <el-button round type="info" v-if="!state.hasLogan" @click="state.loginDialogShow=true">登录</el-button>
        <el-dialog v-model="state.loginDialogShow">
           <login-view></login-view>
        </el-dialog>
        <div class="info" v-if="state.hasLogan">
          <el-avatar :src="imgSrc(state.user.avatar)"></el-avatar>
          <el-text style="margin-left:2px">{{state.user.name}}</el-text>
        </div>
    </div>
       <video-player :src="videoUrl(state.video.name)" :coverSrc="imgSrc(state.video.cover)" :isM3U8="state.isM3U8" 
       :currentTime="state.currentTime" :hasLogan="state.hasLogan" :videoId="state.video.id" ></video-player>
       <p class="description" v-html="state.video.description"></p>
    </div>
</template>

<script setup>
import {  DownloadVideo, GetCurrentTime, GetVideo } from "@/api/Video";
import { reactive,onMounted } from "vue";
import { useRoute } from "vue-router";
import { videoSrc,imgSrc } from "@/modules/AxiosHelper";
import { ElMessageBox } from "element-plus";
import stateStroge from "@/modules/StateStorage";
import { VerifyToken } from "@/api/User";
import { closePage, freshPage} from "@/modules/domHelper";
import { getFileSuffix, makeDownloadAnchor } from "@/modules/Common";


const route = useRoute();
const state = reactive({
     video:null,
     hasLogan:false,
     isM3U8:false,
     user:{
        name:"",
        avatar:""
     },
     loginDialogShow:false,
     currentTime:0,
});

onMounted(async()=>{
    await getData();
});

async function getData(){
   await checkHasLogan();
   const kvId = route.params.id;
   const res = await GetVideo(kvId);
   if(!res.successful){
    ElMessageBox({
        confirmButtonText:"确定",
        title:"视频资源不存在！",
        message:res.message
    }).then(()=>closePage()).catch(()=>closePage());
    return;
   } 
   state.video = res.data;
   if(state.hasLogan){
    state.user.name = stateStroge.get("name");
    state.user.avatar = stateStroge.get("avatar");
    const res = await GetCurrentTime(stateStroge.get("uid"),state.video.id);
    if(res==null)return;
    state.currentTime = res;
  }
}

function videoUrl(name)
{
    if(name.lastIndexOf('.')>=0) return videoSrc(name);
    else{
        state.isM3U8 = true;
        return videoSrc(`${name}/${name}_index.m3u8`);
    }
    
}

async function checkHasLogan() {
  if(!stateStroge.has("token")){
       state.hasLogan = false;
  }
  else{
    const res = await VerifyToken(stateStroge.get("uid"),stateStroge.get("token"));
    if(res==null)return;
    state.hasLogan = res;
    if(state.hasLogan)
    {
      state.user.name = stateStroge.get("name");
      state.user.avatar = stateStroge.get("avatar");
    }
  }
}

async function download() {
    if(state.isM3U8)
       {
        const url = videoSrc(`${state.video.name}/${state.video.title}.zip`);
        const title = state.video.title + ".zip";
        makeDownloadAnchor(url,title);
       }
    else
       {
        const url = (await DownloadVideo(state.video.name)).toString();
        const fileName = `${state.video.title}.${getFileSuffix(state.video.name)}`;
        makeDownloadAnchor(url,fileName);
        const timer = setTimeout(()=>{
            freshPage();
            clearTimeout(timer);
        },500);
       }
      
}


</script>

<style scoped>
#video-detail
{
    position: relative;
    padding: 20px;
}

.title
{
    text-align: left;
    margin-bottom:8px;
}

.description
{
    font-size: 14px;
    text-align: left;
    text-indent: 2em;
}

.user{
    display: flex;
    position: relative;
    height: 50px;
    width: 100%;
    justify-content: flex-end;
    align-items: center;
}
.user .info
{
    display: flex;
    align-items: center;
    position: relative;
}

.user .download
{
    position: absolute;
    left: 5px;
    font-size: 24px;
    color:black;
}
.user .download a{
    text-decoration: none;
    color: black;
}

.user .download a:hover
{
    cursor: pointer;
    color:rgb(0, 74, 235)
}
</style>