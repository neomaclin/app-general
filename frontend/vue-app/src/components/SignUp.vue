<template>
    <div class="col-md-12">
    <div class="card card-container">
      <img
        id="profile-img"
        src="//ssl.gstatic.com/accounts/ui/avatar_2x.png"
        class="profile-img-card"
      />
      <Form @submit="onSubmit" :validation-schema="schema">
        <div v-if="!successful">
          <div class="form-group">
            <label for="login">Username</label>
            <Field name="login" type="text" class="form-control" />
            <ErrorMessage name="login" class="error-feedback" />
          </div>
          <div class="form-group">
            <label for="email">Email</label>
            <Field name="email" type="email" class="form-control" />
            <ErrorMessage name="email" class="error-feedback" />
          </div>
          <div class="form-group">
            <label for="phone">Password</label>
            <Field name="phone" type="phone" class="form-control" />
            <ErrorMessage name="phone" class="error-feedback" />
          </div>
          <div class="form-group">
            <label for="password">Password</label>
            <Field name="password" type="password" class="form-control" />
            <ErrorMessage name="password" class="error-feedback" />
          </div>
          <div class="form-group">
            <button class="btn btn-primary btn-block" :disabled="loading">
              <span
                v-show="loading"
                class="spinner-border spinner-border-sm"
              ></span>
              Sign Up
            </button>
          </div>
          <button type="submit" :disabled="isSubmitting">Submit</button>
        </div>
      </Form>
      <div
        v-if="message"
        class="alert"
        :class="successful ? 'alert-success' : 'alert-danger'"
      >
        {{ message }}
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useForm } from 'vee-validate';

import * as yup from "yup";

import { reactive, ref, inject } from 'vue'
import AuthService from '../services/auth.service';

import { useRouter } from 'vue-router'
import { useStore } from 'vuex'


const store = useStore()
const router = useRouter()
const successful = ref(false)
const loading = ref(false)
const message = ref("")


const schema = yup.object({
      login: yup
        .string()
        .required("Username is required!")
        .min(3, "Must be at least 3 characters!")
        .max(20, "Must be maximum 20 characters!"),
      email: yup
        .string()
        .required("Email is required!")
        .email("Email is invalid!")
        .max(50, "Must be maximum 50 characters!"),
      password: yup
        .string()
        .required("Password is required!")
        .min(6, "Must be at least 6 characters!")
        .max(40, "Must be maximum 40 characters!"),
      phone: yup
        .string().optional(),
    });

const { handleSubmit, isSubmitting } =  useForm({
      validationSchema: schema,
    });


const onSubmit = handleSubmit((values, { resetForm }) => {
    store.dispatch('register', {
       login : values.login!,
       email: values.email!,
       password: values.password!,
       phone: values.phone,
    }).then( ()=> router.push({ name: 'Pending' }))
  });

</script>
