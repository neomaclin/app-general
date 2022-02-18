import { createStore } from "vuex";
import AuthService from "../services/auth.service";
import { AuthState } from "./auth.module";
import auth from "./auth.module";

const store = (service: AuthService) => {
  const authModule = auth(service);
  createStore<AuthState>({
    modules: { authModule },
  });
};

export default store;
