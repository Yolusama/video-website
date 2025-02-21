<template>

    <div class="video-player" >
        <video controls :autoplay="autoPlay" class="video-js" :data-setup="{}">
            <source :src="state.source.url" :type="state.source.type" />
        </video>
    </div>


</template>

<script setup>
import { SaveVideoRecord } from '@/api/Video';
import { getElement } from '@/modules/domHelper';
import stateStroge from '@/modules/StateStorage';
import videojs from 'video.js';
import 'video.js/dist/video-js.css';
import 'videojs-contrib-hls';
import {ref,onMounted,reactive,defineProps,onBeforeUnmount, watch} from "vue";

const state = reactive({
    source:{
      url:"",
      type:""
    },
    totalLength:0
});

const instance = ref(null);

onMounted(()=>{
   state.source.url = src.value;
   if(isM3U8.value)
   state.source.type = "application/x-mpegURL";
   const timer = setTimeout(()=>{
    assignVideoPlayer();
    window.onunload =async ()=>{
       if(!hasLogan.value)return;
      const res = await SaveVideoRecord({
        userId: stateStroge.get("uid"),
        videoId: videoId.value,
        currentTime: instance.value.currentTime(),
        totalTime: state.totalLength
       });
       if(!res) currentTime.value = 0;
    };
    clearTimeout(timer);
   },300);
});

const pros = defineProps({
    src:String,
    autoPlay:Boolean,
    isM3U8:Boolean,
    hasLogan:Boolean,
    coverSrc:String,
    currentTime:Number,
    videoId:String
});

const src = ref(pros.src);
const autoPlay = ref(pros.autoPlay);
const coverSrc = ref(pros.coverSrc);
const isM3U8 = ref(pros.isM3U8);
const currentTime = ref(pros.currentTime);
const hasLogan = ref(pros.hasLogan);
const videoId = ref(pros.videoId);

function assignVideoPlayer()
{
  const videoElement = getElement(".video-player video");
      instance.value = videojs(videoElement,{
        controls:true,
        bigPlayButton: true,
        poster:coverSrc.value,
        playbackRates: [0.5, 1, 1.5, 2,2.5,3.0,5.0,10.0],
        preload:'auto',
         controlBar: { // 设置控制条组件
                //  设置控制条里面组件的相关属性及显示与否
                currentTimeDisplay: true,
                timeDivider: true,
                durationDisplay: true,
                remainingTimeDisplay: true,
                volumePanel: {
                inline: false,
                },
                pictureInPictureToggle: false,
            },
        sources:[
          {
            src: state.source.url,
            type: state.source.type
          }
        ]
      },function(){
        const player = this;
        window.addEventListener("keydown",function(event){
           switch(event.keyCode)
           {
            case 32:
                if(player.paused())
                   player.play();
                else player.pause();
                break;
            case 37: player.currentTime(player.currentTime()-15);break;
            case 38: player.volume(player.volume()+0.1);break;
            case 39: player.currentTime(player.currentTime()+15);break;
            case 40: player.volume(player.volume()-0.1);break;
           }
        });
       player.on("loadedmetadata",()=>{
        state.totalLength = player.duration();
       });
        player.currentTime(currentTime.value);
        if(autoPlay.value||currentTime.value>0)
        {
          this.muted(true);
          this.play();
          const timer = setTimeout(()=>{
                player.muted(false);
                clearTimeout(timer);
          },500);
        }
      });
}

onBeforeUnmount(()=>{
   if(instance.value!=null)
   {
    instance.value.pause();
    instance.value.dispose();
    instance.value = null;
   }
});

watch(() => pros.currentTime, (newTime) => {
  currentTime.value = newTime;});


</script>

<style scoped>
.video-player{
    position: relative;
    height: 600px;
    display: flex;
    justify-content: flex-start;
}

.video-js {
  height: 600px;
  width: 100%;
}


</style>