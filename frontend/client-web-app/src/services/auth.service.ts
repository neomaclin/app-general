import { AxiosStatic } from "axios";
import { LoginRequest, LoginResponse, RegisterRequest, RegisterResponse } from "../types/auth.module";

class AuthService {
   private axios: AxiosStatic
   
   constructor(axios: AxiosStatic) {
        this.axios = axios
   }

   login(user:LoginRequest):Promise<LoginResponse> {
      return this.axios
        .post('/user/login', {
          username: user.login,
          email: user.email,
          password: user.password,
          phone: user.phone
        })
        .then(response => {
          if (response.data.accessToken) {
            localStorage.setItem('user', JSON.stringify(response.data));
          }
          return response.data;
        });
    }

    logout() {
      localStorage.removeItem('user');
    }

    register(user: RegisterRequest):Promise<RegisterResponse> {
      return this.axios.post('/user/register', {
        login: user.login,
        email: user.email,
        password: user.password
      }).then(reponse => reponse.data);
    }
  }

  export default AuthService