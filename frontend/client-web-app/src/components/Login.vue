<template>
  <div class="col-md-12">
    <div class="card card-container">

      <form @submit="onSubmit" :validation-schema="schema">
        <div class="form-group">
          <label for="login">Login</label>
          <input name="login" type="text" class="form-control" v-model="login"  />
          <span>{{ loginError }}</span>
        </div>
        <div class="form-group">
          <label for="password">Password</label>
          <input name="password" type="password" class="form-control"  v-model="password"  />
                          <span>{{ passwordError }}</span>
        </div>
        <div class="form-group">
          <button class="btn btn-primary btn-block" :disabled="isSubmitting">
            <span
              v-show="isSubmitting"
              class="spinner-border spinner-border-sm"
            ></span>
            <span>Login</span>
          </button>
        </div>
        <div class="form-group">
          <div v-if="message" class="alert alert-danger" role="alert">
            {{ message }}
          </div>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useForm, useField } from 'vee-validate';

import {string, object} from "yup";

import { reactive, ref, inject } from 'vue'
import AuthService from '../services/auth.service';

import { useRouter } from 'vue-router'
import { useStore } from 'vuex'
import { key } from '../store'

const store = useStore(key)
const router = useRouter()
// const successful = ref(false)
const message = ref("")

const schema = object({
      login: string().required("You can login with your username/email/phone-number."),
      password: string().required("Password is required!"),
    });

const { handleSubmit, isSubmitting } =  useForm({
      validationSchema: schema,
    });

const { value: login, errorMessage: loginError } = useField('login');

const { value: password, errorMessage: passwordError } = useField('password');

const onSubmit = handleSubmit((values, { resetForm }) => {
    store.dispatch('login', {
       login: values.login,
       email: values.login,
       password: values.password!,
       phone: values.login,
    }).then( ()=> router.push({ name: 'Pending' }))
  });

</script>
<style scoped>

</style>
