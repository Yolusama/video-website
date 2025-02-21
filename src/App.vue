<template>
  <router-view></router-view>
</template>

<script>
import { ref, onMounted,onBeforeUnmount} from "vue";
import { Get } from "./modules/AxiosHelper";

export default {
  name: "App",
  setup() {
    const timer = ref(0);
    const expire = ref(1000 * 60 * 3);

    onMounted(() => {
      timer.value = setInterval(async () => {
        await Get("/Common/HeartBeat");
      }, expire.value);
    });

    onBeforeUnmount(function(){
          clearInterval(timer.value);
    });

    return {
      timer,
      expire,
    };
  },
};
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}
</style>
