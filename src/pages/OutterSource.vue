<template>
    <div id="outter-src">
        <video id="video" :data-set-up="{}"  class="video-js">
          <source :src="src" :type="type" />
        </video>
    </div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';
import { getElement } from '@/modules/domHelper';
import videojs from 'video.js';
import 'video.js/dist/video-js.css';
import 'videojs-contrib-hls';

const route = useRoute();
const src = ref("");
const type = ref("");
onMounted(async() => {
    const source = route.query["src"];
    const suffix = source.substring(source.lastIndexOf('.'));
    const isM3U8 = suffix  == ".m3u8";
    if (isM3U8)
        type.value = "application/x-mpegURL";
    else
       type.value = `video/${suffix.substring(1)}`
    src.value = source;
    const videoElement = getElement("#video");
    videojs(videoElement, {
        controls: true,
        bigPlayButton: true,
        playbackRates: [0.5, 1, 1.5, 2, 2.5, 3.0, 5.0, 10.0],
        preload: 'auto',
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
        sources: [
            {
                src: src.value,
                type: type.value
            }
        ]
    }, function () {
        const player = this;
        window.addEventListener("keydown", function (event) {
            switch (event.keyCode) {
                case 32:
                    if (player.paused())
                        player.play();
                    else player.pause();
                    break;
                case 37: player.currentTime(player.currentTime() - 15); break;
                case 38: player.volume(player.volume() + 0.1); break;
                case 39: player.currentTime(player.currentTime() + 15); break;
                case 40: player.volume(player.volume() - 0.1); break;
            }
        });
    });
});

</script>

<style>
#outter-src {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 94vh;
    width: 100vw;
}

#video {
    min-width: 30%;
    min-height: 35%;
    max-height: 95%;
    max-width: 96%;
}
</style>