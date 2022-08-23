import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  base: '/tt1/',
  // server: {
  //   proxy: {
  //     '/api': {
  //       target: 'http://202.44.9.103:8080/tt1/',
  //       // "pathRewrite": { '^/api': '' },
  //       changeOrigin: true,
  //       secure: false,
  //       rewrite: (path) => {
  //         console.log(path);
  //         return path.replace('/^\/api/', '')
  //       }
  //       // rewrite: (path) => path.replace(/^\/api/, '')
  //     }
  //   },
  //   port: 80
  // }
})

