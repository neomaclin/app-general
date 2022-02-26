<template>
  <div class="col-md-12">
    <div class="card card-container">
      <form @submit="onSubmit" :validation-schema="schema">
        <div class="form-group">
          <label for="username">Username</label>
          <input
            name="username"
            type="text"
            class="form-control"
            v-model="username"
          />
          <span>{{ usernameError }}</span>
        </div>
        <div class="form-group">
          <label for="email">Email</label>
          <input
            name="email"
            type="email"
            class="form-control"
            v-model="email"
          />
          <span>{{ emailError }}</span>
        </div>
        <div class="form-group">
          <label for="phone">Phone Number</label>
          <input
            name="phone"
            type="phone"
            class="form-control"
            v-model="phone"
          />
          <span>{{ phoneError }}</span>
        </div>
        <div class="form-group">
          <label for="password">Password</label>
          <input
            name="password"
            type="password"
            class="form-control"
            v-model="password"
          />
          <span>{{ passwordError }}</span>
        </div>
        <div class="form-group">
          <button class="btn btn-primary btn-block" :disabled="isSubmitting">
            <span
              v-show="isSubmitting"
              class="spinner-border spinner-border-sm"
            ></span>
            Sign Up
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useForm, useField } from "vee-validate";
import { key } from "../store";
import { object, string } from "yup";

import { useRouter } from "vue-router";
import { useStore } from "vuex";
import { RegisterRequest } from "../types/auth.module";

const store = useStore(key);
const router = useRouter();

const schema = object({
  username: string()
    .required("Username is required!")
    .min(3, "Must be at least 3 characters!")
    .max(20, "Must be maximum 20 characters!"),
  email: string()
    .required("Email is required!")
    .email("Email is invalid!")
    .max(50, "Must be maximum 50 characters!"),
  phone: string().notRequired(),
  password: string()
    .required("Password is required!")
    .min(6, "Must be at least 6 characters!")
    .max(40, "Must be maximum 40 characters!"),
});

const { handleSubmit, isSubmitting } = useForm({
  validationSchema: schema,
});

const { value: username, errorMessage: usernameError } = useField("username");
const { value: phone, errorMessage: phoneError } = useField("phone");
const { value: email, errorMessage: emailError } = useField("email");
const { value: password, errorMessage: passwordError } = useField("password");

const onSubmit = handleSubmit(values => {
  const payload: RegisterRequest = {
      login: values.username!,
      email: values.email!,
      password: values.password!,
      phone: values.phone ? values.phone! : null
    }
    store.dispatch('register', payload, {root: true} )
         .then(() => router.push({ name: "Pending" }));
});
  //  const onSubmit = handleSubmit(values => {
  //     alert(JSON.stringify(values, null, 2));
  //   })
</script>
