import { createApp } from 'vue'
import App from './App.vue'
import router from './router' // <---
import {key, store} from './store'

import axios from 'axios'
import VueAxios from 'vue-axios'
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap"
import { FontAwesomeIcon } from './plugins/font-awesome'
import AuthService from './services/auth.service'

const instance = axios.create({
   baseURL: 'https://some-domain.com/api/',
   timeout: 1000,
   headers: {'X-Custom-Header': 'foobar'}
 });

const authservice = new AuthService(instance)
const authstore = store(authservice)

createApp(App)
   .use(router)
   .use(authstore, key)
   .use(VueAxios, axios)
   .component("font-awesome-icon", FontAwesomeIcon)
   .provide('axios', axios)
   .mount('#app')

