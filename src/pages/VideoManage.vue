<template>
  <div id="video">
    <header>
     <el-input v-model="state.query.title" placeholder="视频标题搜索" style="width:20%"></el-input>
     <div class="select">
      <span>视频类型：</span>
     <el-radio-group v-model="state.query.type">
       <el-radio label="">
          全部
        </el-radio>
        <el-radio label="1">
          普通视频
        </el-radio>
         <el-radio label="2">
          电影
        </el-radio>
         <el-radio label="3">
          连续剧
        </el-radio>
         <el-radio label="4">
          短视频
        </el-radio>
     </el-radio-group>
    </div>
    <div class="select">
      <span>视频状态：</span>
     <el-radio-group v-model="state.query.status">
      <el-radio label="">
          全部
        </el-radio>
        <el-radio label="1">
          正常
        </el-radio>
        <el-radio label="0">
          异常
        </el-radio>
     </el-radio-group>
    </div>
    <div style="margin-top:3px;display:flex;align-items: center;">
      <span>发布年份：</span>
    <el-input :min="1990" :max="2026" v-model="state.query.year" style="width:55px" maxlength="4"></el-input>
  </div>
    <el-button type="primary" @click="getData" style="margin-left:10px;margin-top:3px">
      <el-icon><Search/></el-icon>
    </el-button>
  
    </header>
     <el-table :data="state.videos" border>
      <el-table-column label="视频标识" prop="id" width="140"> </el-table-column>
      <el-table-column label="视频资源" width="125">
        <template #default="scope">
           <span class="src" @click="previewSource(scope.row)">{{scope.row.name}}</span>
        </template>
      </el-table-column>
      <el-table-column label="视频标题" prop="title" width="160"> </el-table-column>
      <el-table-column label="视频信息" width="130">
        <template #default="scope">
           <el-image :src="imgSrc(scope.row.cover)" preview-teleported="true" style="height:80px;width:80px;border-radius: 5px;"
           :preview-src-list="[imgSrc(scope.row.cover)]">
           </el-image>
           <el-button type="primary" size="small" plain @click="checkDescription(scope.row.description)">视频内容描述</el-button>
        </template>
      </el-table-column>
      <el-table-column label="视频状态">
        <template #default="scope">
          <div class="normal" v-if="scope.row.status==1">
              正常
          </div>
          <div class="abnormal" v-else>
            异常
          </div>
          </template>
      </el-table-column>
      <el-table-column label="视频类型" width="90">
        <template #default="scope">
          <el-tag :type="tagType(scope.row.type)" size="small">{{
            getVideoType(scope.row.type)
          }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" width="150">
        <template #default="scope">
          {{ new Date(scope.row.createTime).toLocaleString() }}
        </template>
      </el-table-column>
      <el-table-column label="发布年份" prop="publishYear" width="100">
      </el-table-column>
      <el-table-column label="更新时间" width="150">
        <template #default="scope">
          {{ new Date(scope.row.updateTime).toLocaleString() }}
        </template>
      </el-table-column>
      <el-table-column width="150">
        <template #default="scope">
          <el-button type="danger" plain size="small" v-if="scope.row.status==1" @click="changeStatus(scope.row,0)">禁用</el-button>
          <el-button type="success" plain size="small" v-else @click="changeStatus(scope.row,1)">恢复</el-button>
          <el-button type="info" plain size="small" @click="toModify(scope.row)">修改</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      v-model:current-page="pagination.current"
      v-model:page-size="pagination.size"
      :size="pagination.size"
      layout="total, prev, pager, next, jumper"
      :total="pagination.total"
      @size-change="getData"
      @current-change="getData"
    />

    <el-dialog v-model="state.dialogShow" title="修改视频信息" @close="clear" style="width:1000px">
          <VideoModify :modified="true" :video="state.selectedVideo" v-if="state.selectedVideo!=null"></VideoModify>
    </el-dialog>
    
    <el-drawer v-model="state.previewVideoDialogShow" title="视频查看" @close="clear"  size="80%">
      <video-player :src="videoSrc(state.selectedVideo.name)" :autoplay="false" :coverSrc="imgSrc(state.selectedVideo.cover)"
      v-if="state.selectedVideo!=null&&state.selectedVideo.isM3U8"></video-player>
      <video :src="videoSrc(state.selectedVideo.name)" controls v-if="state.selectedVideo!=null&&!state.selectedVideo.isM3U8"
       style="width:100%;height:600px"
      >
      </video>
    </el-drawer>
  </div>
</template>

<script setup>
import { copy, PageOption } from "@/modules/Common";
import { reactive,onMounted,ref } from "vue";
import { imgSrc,videoSrc } from "@/modules/AxiosHelper";
import { GetVideos,ChangeStatus  } from "@/api/Video";
import { ElMessageBox } from "element-plus";

const state = reactive({
      videos:[],
      query:{
        title:"",
        type:"",
        status:"",
        year:""
      },
      dialogShow:false,
      selectedVideo:null,
      previewVideoDialogShow:false
});

const pagination=ref(new PageOption(1,4,0));

onMounted(async function(){
  await getData();
});

async function getData()
{
    const res = await GetVideos(pagination.value,state.query.title,state.query.type,state.query.status,state.query.year);
    if(res!=null)
    {
      state.videos = res.data;
      pagination.value.total = res.total;
    }
}

function checkDescription(description)
{
   ElMessageBox({
    confirmButtonText:'确定',
    title:"视频内容描述",
    message:description
   }).catch(()=>{});
}

function tagType(type)
{
  switch(type)
  {
    case 1:return "info";
    case 2:return "primary";
    case 3:return "warning";
    case 4:return "success";
  }
}

function getVideoType(type)
{
  switch(type)
  {
    case 1:return "普通视频";
    case 2:return "电影";
    case 3:return "连续剧";
    case 4:return "短视频";
    case 5:return "纪录片";
  }
}

function toModify(video)
{
  state.selectedVideo = video;
  state.dialogShow = true;
}

function previewSource(video)
{ 
   state.previewVideoDialogShow = true;
   state.selectedVideo = {};
   copy(video,state.selectedVideo);
   state.selectedVideo.isM3U8 = false;
   const srcName = state.selectedVideo.name;
   if(srcName.indexOf('.')<0)
     {
      state.selectedVideo.name = `${srcName}/${srcName}_index.m3u8`;
      state.selectedVideo.isM3U8 = true;
     }
   
}

function clear()
{
  state.selectedVideo = null;
}

async function changeStatus(video,status) {
  const res = await ChangeStatus(status,video.id);
  if(res)
     video.status = status;
}

</script>

<style scoped>
#video
{
  position: relative;
  padding: 2px;
}

#video .el-pagination
{
  margin: 10px auto 0;
  justify-content: center;
}

#video header{
  display: flex;
  margin-bottom: 5px;
  flex-flow: row wrap;
}

#video header .select
{
  margin: 0 8px;
}

#video .src{
  text-decoration: underline;
}

#video .src:hover{
  cursor: pointer;
  color: rgb(0,73,235);
}



</style>