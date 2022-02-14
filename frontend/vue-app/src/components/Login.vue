<template>
    <form  @submit.prevent>
    <img class="mb-4" alt="" width="72" height="57">
    <h1 class="h3 mb-3 fw-normal">Please sign in</h1>

    <div class="form-floating">
      <input type="login" v-model="requestForm.login" class="form-control" id="floatingInput" placeholder="">
      <label for="floatingInput">Login Id</label>
    </div>
    <div class="form-floating">
      <input type="email" v-model="requestForm.email" class="form-control" id="floatingInput" placeholder="name@example.com">
      <label for="floatingInput">Email address</label>
    </div>
    <div class="form-floating">
      <input type="phone" v-model="requestForm.phone" class="form-control" id="floatingInput" placeholder="">
      <label for="floatingInput">Phone Number</label>
    </div>
    <div class="form-floating">
      <input type="password" v-model="requestForm.password" class="form-control" id="floatingPassword" placeholder="Password">
      <label for="floatingPassword">Password</label>
    </div>

    <div class="checkbox mb-3">
      <label>
        <input type="checkbox" value="remember-me"> Remember me
      </label>
    </div>
    <button @click.once="submitRequest" class="w-100 btn btn-lg btn-primary" type="submit">Sign in</button>
    <p class="mt-5 mb-3 text-muted">&copy; 2017–2021</p>
  </form>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { userService } from '../services/UserService';
import { LoginRequest } from '../types/auth.module';

const remember = ref(false)

const requestForm = reactive<LoginRequest>({
  login: "", 
  password:""
})

function submitRequest() {
  userService.login(requestForm)
}

</script>
<style scoped>

</style>
