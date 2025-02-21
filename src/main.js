import { createApp } from 'vue'
import App from './App.vue'
import router from './modules/router';
import ElementPlus from 'element-plus'
import "element-plus/dist/index.css";
import * as ElementPlusIconsVue from "@element-plus/icons-vue";
import VideoModify from "./components/VideoModify.vue";
import VideoPlayer  from './components/VideoPlayer.vue';

const app=createApp(App);

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component);
}

app.component("VideoModify",VideoModify);
app.component("VideoPlayer",VideoPlayer);
app.use(router);
app.use(ElementPlus);
app.mount('#app');

