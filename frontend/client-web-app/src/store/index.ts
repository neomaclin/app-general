import { createStore, Store } from "vuex";
import AuthService from "../services/auth.service";
import { AuthState } from "./auth.module";
import auth from "./auth.module";
import { InjectionKey } from "vue";

export const key: InjectionKey<Store<AuthState>> = Symbol('auth')

export const store = ((service: AuthService) => {
  const authModule = auth(service);
  return createStore<AuthState>({
    modules: { authModule },
  })}
  );

