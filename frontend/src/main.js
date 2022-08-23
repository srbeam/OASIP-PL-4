import { createApp } from 'vue'
import App from './App.vue'
import './index.css'
import 'bootstrap/dist/css/bootstrap.min.css'
import router from './router'


const app = createApp(App)


app.use(router).mount('#app')
