import { createRouter, createWebHistory } from "vue-router";
import HomeView from "../views/HomeView.vue";
import LoginView from "../views/LoginView.vue";
import SignUpView from "@/views/SignUpView.vue";
import FindView from "@/views/FindIdPwView.vue";
import SearchMapView from "@/views/SearchMapView.vue";
// import SignUp from "@/views/SignUp.vue";

const routes = [
  {
    path: "/",
    component: HomeView,
  },
  {
    path: "/login",
    component: LoginView,
  },
  {
    path: "/sign",
    component: SignUpView,
  },
  {
    path: "/find",
    component: FindView,
  },
  {
    path: "/searchMap",
    component: SearchMapView,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
