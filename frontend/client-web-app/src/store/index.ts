import { createStore, Store } from "vuex";
import AuthService from "../services/auth.service";
import { LoginRequest, RegisterRequest } from "../types/auth.module";

import { InjectionKey } from "vue";

export interface AuthState {
  status: {
    loggedIn: boolean;
  };
  user: string | null;
}
const user = localStorage.getItem("user");

export const key: InjectionKey<Store<AuthState>> = Symbol("auth");

export const store = (service: AuthService) =>
  createStore({
    state: user
      ? { status: { loggedIn: true }, user }
      : { status: { loggedIn: false }, user: null },
    mutations: {
      loginSuccess(state, user: string) {
        state.status.loggedIn = true;
        state.user = user;
      },
      loginFailure(state) {
        state.status.loggedIn = false;
        state.user = null;
      },
      logout(state) {
        state.status.loggedIn = false;
        state.user = null;
      },
      registerSuccess(state) {
        state.status.loggedIn = false;
      },
      registerFailure(state) {
        state.status.loggedIn = false;
      },
    },
    actions: {
      login({ commit, state }, user: LoginRequest) {
        return service.login(user).then(
          (user) => {
            commit("loginSuccess", user.accessToken);
            return Promise.resolve(user);
          },
          (error) => {
            commit("loginFailure");
            return Promise.reject(error);
          }
        );
      },
      logout({ commit, state }) {
        service.logout();
        commit("logout");
      },
      register({ commit }, user: RegisterRequest) {
        return service.register(user).then(
          (response) => {
            commit("registerSuccess");
            return Promise.resolve(response);
          },
          (error) => {
            commit("registerFailure");
            return Promise.reject(error);
          }
        );
      },
    },
  });
