import "./assets/main.css";
import "./assets/styles.css";

import { createApp } from "vue";
import { createPinia } from "pinia";
import App from "./App.vue";
import router from "./router";

const app = createApp(App);
const pinia = createPinia();

app.use(pinia); // Pinia 등록
app.use(router);
app.mount("#app");

