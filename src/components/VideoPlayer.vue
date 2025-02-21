<template>

    <div class="video-player" >
        <video controls :autoplay="autoPlay" class="video-js" :data-setup="{}">
            <source :src="state.source.url" :type="state.source.type" />
        </video>
    </div>


</template>

<script setup>
import { getElement } from '@/modules/domHelper';
import videojs from 'video.js';
import 'video.js/dist/video-js.css';
import 'videojs-contrib-hls';
import {ref,onMounted,reactive,defineProps,onBeforeUnmount} from "vue";

const state = reactive({
    source:{
      url:"",
      type:""
    }
});

const instance = ref(null);

onMounted(()=>{
   state.source.url = src.value;
   state.source.type = "application/x-mpegURL";
   const timer = setTimeout(()=>{
    assignVideoPlayer();
    clearTimeout(timer);
   },300);
});

const pros = defineProps({
    src:String,
    autoPlay:Boolean,
    isM3U8:Boolean,
    coverSrc:String
});

const src = ref(pros.src);
const autoPlay = ref(pros.autoPlay);
const coverSrc = ref(pros.coverSrc);

function assignVideoPlayer()
{
  const videoElement = getElement(".video-player video")
      instance.value = videojs(videoElement,{
        controls:true,
        bigPlayButton: true,
        playbackRates: [0.5, 1, 1.5, 2,2.5,3.0,5.0,10.0],
        preload:'auto',
        poster:coverSrc.value,
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
        this.play();

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