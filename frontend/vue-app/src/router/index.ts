import Vue from 'vue';
import Router from 'vue-router';

//
import Home from "../views/Home.vue"
import User from "../views/User.vue"
import About from '../views/About.vue'
import Activation from '../views/Activation.vue'

//
import SignUp from "../components/SignUp.vue"
import Login from "../components/Login.vue"

export const router = Router.createRouter({
  history: Router.createWebHistory(),
  routes: [
    { path: '/', name: 'Home', component: Home , meta: { requiresAuth: false }},
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
      {
        path: 'logout',
        name: 'LogOut',
        redirect : '/',
        // anybody can read a post
        meta: { requiresAuth: true },
        beforeEnter: (to, from) => {
          // reject the navigation
          localStorage.removeItem('user');
          return true
        },
      }

      ] },
    { path: '/about', component: About, meta: { requiresAuth: false } },
    { path: '/activation?key=:key', component: Activation , props: true, meta: { requiresAuth: false } },
    // otherwise redirect to home
    { path: '*', redirect: '/' }
  ]
});

router.beforeEach((to, from, next) => {
  // redirect to login page if not logged in and trying to access a restricted page
 // const publicPages = ['/user/login', '/user/register','/about','/activation'];
  // const authRequired = !publicPages.includes(to.path);
  //const loggedIn = localStorage.getItem('user');

  // if (authRequired && !loggedIn) {
  //   return next('/login');
  // }

  next();
})