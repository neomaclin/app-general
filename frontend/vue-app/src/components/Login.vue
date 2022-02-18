<template>
  <div class="col-md-12">
    <div class="card card-container">
      <img
        id="profile-img"
        src="//ssl.gstatic.com/accounts/ui/avatar_2x.png"
        class="profile-img-card"
      />
      <Form @submit="onSubmit" :validation-schema="schema">
        <div class="form-group">
          <label for="login">Username</label>
          <Field name="login" type="text" class="form-control" />
          <ErrorMessage name="login" class="error-feedback" />
        </div>
        <div class="form-group">
          <label for="password">Password</label>
          <Field name="password" type="password" class="form-control" />
          <ErrorMessage name="password" class="error-feedback" />
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
      </Form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useForm } from 'vee-validate';

import {string, object} from "yup";

import { reactive, ref, inject } from 'vue'
import AuthService from '../services/auth.service';

import { useRouter } from 'vue-router'
import { useStore } from 'vuex'


const store = useStore()
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
