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
routes.push(new Route("/").redirectTo("/Login"));
routes.push(new Route("/Login","Login",()=>import("@/pages/LoginView.vue")));
const home = new Route("/Home","Home",()=>import("@/pages/AppHome.vue"));
home.addSubRoute(new Route("/Home/UserManage","UserManage",()=>import("@/pages/UserManage.vue")));
home.addSubRoute(new Route("/Home/Video/Manage","VideoManage",()=>import("@/pages/VideoManage.vue")));
home.addSubRoute(new Route("/Home/Video/Upload","VideoUpload",()=>import("@/pages/VideoUpload.vue")));
routes.push(home);

const router=createRouter({
       routes:routes,
       history:createWebHashHistory()
});
export default router;