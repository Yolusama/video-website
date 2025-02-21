<template>
    <div class="video">   
        <el-form label-width="auto" label-postion="right">
          <h3 style="margin-bottom: 25px;text-align: left;">{{!modified?"上传视频":"编辑视频"}}</h3>
             <el-form-item label="视频名称" v-if="modified">
                 <el-input type="text" v-model="state.video.name" placeholder="视频名称" readonly="true">
                 </el-input>
             </el-form-item>
             <el-form-item label="视频描述">
                <el-input type="textarea" v-model="state.video.description" :rows="12" resize="none"></el-input>
             </el-form-item>
             <el-form-item label="视频标题">
                <el-input v-model="state.video.title" placehilder="视频标题"></el-input>
             </el-form-item>
             <el-form-item label="上传视频封面">
                <label for="cover-upload">
                <el-icon><Plus/></el-icon>
                <input type="file" style="display:none" id="cover-upload" @change="previewImage" accept="image/*">
                </label>
                <img :src="state.coverUrl" v-if="state.coverUrl.length>0" class="preview-img">
             </el-form-item>
             <el-form-item label="视频类型">
                 <el-radio-group v-model="state.video.type">
                    <el-radio :label="1">普通视频</el-radio>
                    <el-radio :label="2">电影</el-radio>
                    <el-radio :label="3">连续剧</el-radio>
                    <el-radio :label="4">短视频</el-radio>
                    <el-radio :label="5">纪录片</el-radio>
                 </el-radio-group>
             </el-form-item>
             <el-form-item label="上传视频">
                <label label="video-upload">
                    <el-icon><Upload /></el-icon>
                    <input type="file" id="upload-video" @change="previewVideo" style="display:none" accept="video/*"> 
                </label>
                <video controls :src="state.videoUrl" v-if="state.fileType==FileType.ShortVideo&&
                state.videoUrl.length>0" class="preview-video"> 
                </video>
                <span style="color:cyan;line-height: 100px;font-size:16px;margin-right: 5px;"
                v-if="state.selectedVideo!=null&&state.fileType==FileType.LongVideo">大文件不支持预览</span>
                <el-button type="warning" plain v-if="!state.dialogShow&&state.current>0" @click="state.dialogShow=true">查看上传进度</el-button>
               </el-form-item>
               <el-form-item label="发表年份">
                  <el-input min="1990" max="2026" v-model="state.video.publishYear" maxlength="4" style="width:60px">
                  </el-input>
               </el-form-item>
                <el-button type="primary" plain @click="upload">{{modified?"保存":"确定"}}</el-button>
                <el-button type="info" plain>取消</el-button>
        </el-form>
        <el-dialog :modal="false" v-model="state.dialogShow" title="上传长视频" style="width:60%">
              <el-progress :percentage="percentage(state.current,state.end)">
              </el-progress>
        </el-dialog>
    </div>
</template>

<script setup>
import { reactive,ref,onMounted,defineProps } from 'vue';
import { ChunkeFile, FileType, GetTotalChunkedSize, IsBigFile, UploadFile } from '@/api/Common';
import { DefaultVideoCover, previewOpenFile,percentage, getFileSuffix } from '@/modules/Common';
import { SaveVideoInfo, UploadLongVideo } from '@/api/Video';
import { ElNotification } from 'element-plus';
import { freshPage } from '@/modules/domHelper';
import { imgSrc, videoSrc } from '@/modules/AxiosHelper';

const state = reactive({
   video:{
    title:"",
    name:null,
    description:"",
    cover:DefaultVideoCover,
    type:1,
    publishYear:1990
   },
   selectedCover:null,
   selectedVideo:null,
   coverUrl:"",
   videoUrl:"",
   current:0,
   dialogShow:false,
   end:0,
   fileType:0
});

const pros = defineProps({
   video:Object,
   modified:Boolean
});

const video = ref(pros.video);
const modified = ref(pros.modified);

onMounted(()=>{
   if(modified.value)
      {
         state.video = video.value;
         state.coverUrl = imgSrc(state.video.cover);
         if(state.video.type == 4)
            state.videoUrl = videoSrc(state.video.name);
      }
});

function previewImage(event)
{
   const file = event.target.files[0];
   previewOpenFile(file,url=>state.coverUrl=url);
   state.selectedCover = file;
}

function previewVideo(event)
{
   const file = event.target.files[0];
   previewOpenFile(file,url=>state.videoUrl=url);
   state.selectedVideo = file;
   state.fileType= IsBigFile(file)? FileType.LongVideo:FileType.ShortVideo;
}

async function upload()
{
   var res;

   if(state.selectedVideo!=null)
   {
      if(!IsBigFile(state.selectedVideo))
      {
         res = await UploadFile(state.video.type,state.selectedVideo,state.video.name);
         if(res!=null)
         {
            state.video.name = res;
            await saveVideoInfo();
         }
      }
      else{
         state.end = GetTotalChunkedSize(state.selectedVideo.size);
         state.dialogShow = true;
         const timer = setInterval(async()=>{
            if(state.current>state.end)
                {
                  clearInterval(timer);
                  ElNotification({
                     message:"上传完毕",
                     type:"success"
                  });
                  await saveVideoInfo();
                }
             else
             {
               const filePart = ChunkeFile(state.current,state.end,state.selectedVideo);
               const res = await UploadLongVideo(state.current,filePart,state.end,state.video.name,
               getFileSuffix(state.selectedVideo.name),state.video.title
             );
             if(state.current==0)
                state.video.name = res.name;
             if(res.ok)
             {
               state.current++;
             }
             else{
               clearInterval(timer);
             }
            }
         },500);
      }
   }
   else {
      if(state.selectedCover!=null)
     {
      res = await UploadFile(FileType.VideoCover,state.selectedCover,state.video.cover);
      if(res!=null)
        state.video.cover = res;
      await saveVideoInfo();
     }
   }

}

async function saveVideoInfo()
{
   const res = await SaveVideoInfo(state.video);
   if(res!=null)
   {
      state.video.id = res.id;
      freshPage();
   }
}


</script>

<style scoped>
.video 
{
   position:relative;
   width: 1000px;
}

.video .el-form
{
   width: 750px;
   margin: 0 auto;
}

.video label
{
   display: flex;
   justify-content: flex-start;
}

.video label .el-icon
{
   font-size: 30px;
   width: 100px;
   height: 100px;
}

.video label .el-icon:hover{
   color:rgb(0,73,242);
   cursor: pointer;
   border: 1px rgb(0,73,242) dashed;
}

.video .preview-img{
   width: 100px;
   height: 100px;
   border-radius: 3px;
   margin-left: 12px;
}

.video .preview-video
{
   display: inline-block;
   width: 60%;
   height: 140px;
   margin-left: 14px;
}


</style>