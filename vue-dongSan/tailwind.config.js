/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
    "./src/components/AppHeader.vue", // Vue와 관련된 파일 경로
  ],
  theme: {
    extend: {},
  },
  plugins: [],
};
