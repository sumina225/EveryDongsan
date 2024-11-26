import { fileURLToPath, URL } from "node:url";
import legacy from "@vitejs/plugin-legacy";
import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";
import vueDevTools from "vite-plugin-vue-devtools";
import commonjs from "vite-plugin-commonjs";

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue(), vueDevTools(), legacy(), commonjs()],
  resolve: {
    alias: {
      "@": fileURLToPath(new URL("./src", import.meta.url)),
    },
  },
  optimizeDeps: {
    include: ["jwt-decode"], // 여기에 jwt-decode 추가
  },
  server: {
    proxy: {
      "/member": {
        target: "http://localhost:8080", // 백엔드 주소
        changeOrigin: true,
        secure: false,
      },
    },
  },
});
