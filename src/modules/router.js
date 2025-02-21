import { createRouter,createWebHashHistory } from "vue-router";

class Route {
  constructor(path, name, component) {
    this.path = path;
    this.name = name;
    this.component = component;
    this.children = [];
  }

  addSubRoute(route) {
    this.children.push(route);
  }

  redirectTo(path) {
    this.redirect = path;
    return this;
  }
}


const routes=[];
routes.push(new Route("/").redirectTo("/Home"));
const home = new Route("/Home","Home",()=>import("@/pages/AppHome.vue"));
routes.push(home);
routes.push(new Route("/VideoDetail/:id","VideoDetail",()=>import("@/pages/VideoDetail.vue")))
routes.push(new Route("/SelfInfo","SelfInfo",()=>import("@/pages/SelfInfo.vue")));

const router=createRouter({
       routes:routes,
       history:createWebHashHistory()
});
export default router;