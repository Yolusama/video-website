import { createApp } from 'vue'
import App from './App.vue'
import router from './modules/router';
import ElementPlus from 'element-plus'
import "element-plus/dist/index.css";
import * as ElementPlusIconsVue from "@element-plus/icons-vue";
import VideoPlayer  from './components/VideoPlayer.vue';
import LoginView from './components/LoginView.vue';

const app=createApp(App);

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component);
}

app.component("VideoPlayer",VideoPlayer);
app.component("LoginView",LoginView);
app.use(router);
app.use(ElementPlus);
app.mount('#app');

