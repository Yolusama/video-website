<template>
  <div id="home">
   <header>
    <div class="id-search">
         <el-input type="text" v-model="state.kvId" placeholder="kv视频号搜索">
            <template #append>
               <el-button type="primary" @click="seeDetail(state.kvId)">
                  <el-icon><Search/></el-icon>
               </el-button>
            </template>
        </el-input>
      </div>
      <div class="search">
        <el-input v-model="state.query.title" placeholder="视频标题搜索" style="width:230px"></el-input>
       <div class="type"> 
        <span>视频类型：</span>
        <el-radio-group v-model="state.query.type">
          <el-radio label="">
             不限
          </el-radio>
          <el-radio :label="1">
            普通视频
          </el-radio>
          <el-radio :label="2">
             电影
          </el-radio>
          <el-radio :label="3">
            连续剧
          </el-radio>
          <el-radio :label="4">
            短视频
          </el-radio>
          <el-radio :label="5">
            纪录片
          </el-radio>
        </el-radio-group>
        </div>
        <div class="year">
        <span>发布年份：</span>
        <el-input v-model="state.query.publishYear" max="2026" min="1990" maxlength="4" style="width:65px"></el-input>
      </div>
       <el-button type="primary" style="margin-left:13px" @click="getData">
        <el-icon><Search/></el-icon>搜索
      </el-button>
      <div class="user">
      <el-button round type="info" v-if="!state.hasLogan" @click="state.loginDialogShow=true">登录</el-button>
        <el-dialog v-model="state.loginDialogShow">
           <login-view></login-view>
        </el-dialog>
        <el-popover>
          <template #reference>
            <div class="info" v-if="state.hasLogan">
          <el-avatar :src="imgSrc(state.user.avatar)"></el-avatar>
          <el-text style="margin-left:2px">{{state.user.name}}</el-text>
             </div>
          </template>
            <div class="funcs">
              <div class="func" @click="seeSelfInfo">个人中心</div>
               <div class="func" @click="state.recordShow=true">历史记录</div>
               <div class="func" @click="logout">退出登录</div>
            </div>
      </el-popover>
    </div>
      </div>   
   </header>

   <el-drawer v-model="state.recordShow" size="40%" @open="getVideoRecord" @close="clearData">
     <div class="record-content">
      <div style="margin-bottom: 3px;display:flex;justify-content: flex-end;"><el-button type="danger" plain @click="clearRecord">清空历史记录</el-button></div>
      <div class="record" v-for="(item,index) in state.record.data" :key="index" @click="seeHistory(item.videoId)">
        <el-image :src="imgSrc(item.videoCover)">
        </el-image>
        <div class="info">
          <span class="title">
            {{ item.videoTitle }}
          </span>
          <span class="time">
           上次观看： {{ new Date(item.updateTime).toLocaleString() }}
          </span>
          <span class="process">
           已观看： {{ percentage(item.currentTime,item.totalTime) + "%" }}
          </span>
        </div>
        <el-icon class="delete" @click="removeRecord($event,index)"><Close/></el-icon>
      </div>
     </div>
     <el-pagination 
     background layout="prev, pager, next,jumper,total"
     :total="state.record.pagination.total"
     v-model:current-page="state.record.pagination.current"
     v-model:page-size="state.record.pagination.size"
     :size="state.record.pagination.size"
     @current-change="getVideoRecord"
     style="margin-top:5px"
     >
     </el-pagination>
   </el-drawer>

   <div class="content">
     <div class="video" v-for="(video,index) in state.videos" :key="index" @click="seeDetail(video.id)">
       <el-image :src="imgSrc(video.cover)"></el-image>
       <div class="info">
        <span class="title">
          {{ video.title }}
        </span> 
        <span class="year">{{video.publishYear}}年</span>
        <p class="description">{{video.description}}</p>
       </div>
     </div> 
     <el-pagination 
     background layout="prev, pager, next,jumper,total"
     :total="pagination.total"
     v-model:current-page="pagination.current"
     v-model:page-size="pagination.size"
     :size="pagination.size"
     @current-change="getData"
     style="margin-top:5px"
     >
     </el-pagination>
   </div>
   <footer>
       @copyright 视频网站，小康制作
   </footer>
  </div>
</template>
<script setup>
import { ref,reactive,onMounted } from "vue";
import { imgSrc } from "@/modules/AxiosHelper";
import { PageOption,percentage } from "@/modules/Common";
import { ClearRecord, GetVideoRecord, GetVideos, RemoveRecord } from "@/api/Video";
import { divePage, freshPage, openPage } from "@/modules/domHelper";
import stateStroge from "@/modules/StateStorage";
import { RemoveToken, VerifyToken } from "@/api/User";

const state = reactive({
    hasLogan:false,
    kvId:"",
    query:{
      title:"",
      type:"",
      publishYear:""
    },
    videos:[],
    loginDialogShow:false,
    user:{
      avatar:"",
      name:""
    },
    recordShow:false,
    record:{
      data:[],
      pagination: new PageOption(1,6)
    }
});

const pagination = ref(new PageOption(1,5));

onMounted(async function(){
    await checkHasLogan();
    await getData();
});

async function getData() {
  const res = await GetVideos(pagination.value,state.query.title,state.query.type,state.query.publishYear);
  if(res==null)return;
  state.videos = res.data;
  pagination.value.total = res.total;
}

function seeDetail(kvId)
{
  openPage(`#/VideoDetail/${kvId}`);
}

function seeHistory(kvId)
{
  divePage(`#/VideoDetail/${kvId}`);
}

async function checkHasLogan() {
  if(!stateStroge.has("token")){
       state.hasLogan = false;
  }
  else{
    const res = await VerifyToken(stateStroge.get("uid"),stateStroge.get("token"));
    if(res==null){
      stateStroge.clear();
      return;
    }
    state.hasLogan = res;
    if(state.hasLogan)
    {
      state.user.name = stateStroge.get("name");
      state.user.avatar = stateStroge.get("avatar");
    }
    else{
      stateStroge.clear();
    }
  }
}

async function getVideoRecord() {
  const res = await GetVideoRecord(state.record.pagination,stateStroge.get("uid"));
  if(res==null)return;
  state.record.data = res.data;
  state.record.pagination.total = res.total;
}

function clearData(){
 state.record.data.splice(0,state.record.data.length);
 state.record.total = 0;
}

function seeSelfInfo()
{
  divePage("#/SelfInfo");
}

async function removeRecord(event,index) {
  event.stopPropagation();
  const record = state.record.data[index];
  const res = await RemoveRecord(stateStroge.get("uid"),record.videoId);
  if(res)
     state.record.data.splice(index,1);
}

async function clearRecord() {
  const res = await ClearRecord(stateStroge.get("uid"));
  if(res)
    {
      state.record.data.splice(0,state.record.data.length);
      state.record.total = 0;
    }
}

async function logout() {
  const res = await RemoveToken(stateStroge.get("uid"));
  if(res){
    stateStroge.clear();
    freshPage();
  }
}


</script>
<style scoped>
  #home
  {
    position: relative;
    width: 100%;
    height: 730px;
  }

  #home footer{
    width: 100%;
    height: 60px;
    line-height: 60px;
    background-color: rgb(89, 116, 171);
    position: absolute;
    bottom: 0;
    color: white;
  }
 
  #home .id-search{
    width: 80%;
    margin: 10px auto;
  }

  #home header .search
  {
    position: relative;
    display: flex;
    height: 35px;
    justify-content: center;
  }

#home header .type
{
  margin: 0 12px
}

#home .video
{
  display: flex;
  height: 100px;
  width:60%;
  margin-bottom: 5px;
  align-items: center;
  justify-content: center;
  border: 1px dashed lightgreen;
  border-radius: 4px
  }

#home .record-content{
    width: 90%;
    height: 540px;
  }
#home .record{
  position: relative;
   height: 94px;
   width: 100%;
   display: flex;
   align-items: center;
   border: 1px dashed transparent;
   padding: 4px;
    }

#home .content
{
   display: flex;
   flex-flow: column nowrap;
   height: 635px;
   align-items: center;
   margin-top: 5px;
}

  #home .video .el-image,#home .record .el-image{
    width: 90px;
    height: 90px;
    border-radius: 3px;
  }

  #home .video .info,#home .record .info{
      display: flex;
      flex-flow: column nowrap;
      margin-left: 3px;
  }
#home .record .info{
     width: 150px;
     text-align: left;
  }

#home .video .info .title,#home .record .info .title{
    width: 400px;
    text-overflow: ellipsis;
    overflow: hidden;
    white-space: nowrap;
    font-size: 16px;
    color:gray
}

#home .record .info .title{
  margin-bottom: 5px;
}

#home .video .info .year,#home .record .info .time{
  width: 90%;
  text-align: right;
  color:black;
  font-size: 14px;
}

#home .record .info .time{
  width: 230px;
}

#home .video .info .description
{
   display: -webkit-box;
  -webkit-box-orient: vertical;
  width: 700px;
  text-overflow: ellipsis;
  overflow: hidden;
  height: 60px;
  text-align: left;
  text-indent: 2em;
  -webkit-line-clamp: 3;
  font-size: 15px;
}

#home .video:hover,#home .record:hover{
  cursor: pointer;
  border-color: rgb(0,73,223)
}

#home .user{
  margin-left: 8px;
}

#home .user .info{
  display: flex;
  align-items: center;
}

.funcs{
  display: flex;
  flex-flow: column nowrap;
  justify-content: center;
  align-items: center;
}

.funcs .func{
  margin-bottom: 10px;
}

.funcs .func:hover{
  color: rgb(0,74,232);
  text-decoration: underline;
  cursor: pointer;
}

#home .record .delete{
  position: absolute;
  right: 6px;
  top: 5px;
  color:red;
  z-index:99;
}
</style>