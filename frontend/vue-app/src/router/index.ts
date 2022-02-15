import { createRouter, createWebHistory } from 'vue-router'

//
import Home from '../views/Home.vue'
import User from '../views/User.vue'
import About from '../views/About.vue'
//import Activation from '../views/Activation.vue'
import NotFound from '../views/NotFound.vue'
//
import SignUp from "../components/SignUp.vue"
import Login from "../components/Login.vue"


export const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', name: 'Home', component: Home },
    { path: '/user', component: User, children: [
      {
        path: 'register',
        name: 'Register',
        component: SignUp,
        // only authenticated users can create posts
        meta: { requiresAuth: false }
      },
      {
        path: 'login',
        name: 'Login',
        component: Login,
        // anybody can read a post
        meta: { requiresAuth: false }
      },
      // {
      //   path: 'logout',
      //   name: 'LogOut',
      //   redirect : '/',
      //   // anybody can read a post
      //   meta: { requiresAuth: true },
      //   beforeEnter: (to, from) => {
      //     // reject the navigation
      //     localStorage.removeItem('user');
      //     return true
      //   },
      // }

      ] },
    { path: '/about', name: 'About', component: About },
  //  { path: '/activation?key=:key', component: Activation , props: true, meta: { requiresAuth: false } },
    // otherwise redirect to home
    { path: '/:pathMatch(.*)*', name: 'NotFound',  component: NotFound }
  ]
});