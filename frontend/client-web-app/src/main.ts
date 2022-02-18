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

const authservice = new AuthService(axios)
const authstore = store(authservice)

createApp(App)
   .use(router)
   .use(authstore, key)
   .use(VueAxios, axios)
   .component("font-awesome-icon", FontAwesomeIcon)
   .provide('axios', axios)
   .mount('#app')

