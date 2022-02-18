 
export interface User{
    login: string;
    email: string;
    role: string;
}

export interface RegisterRequest {
    login: string;
    email: string;
    password : string
    phone?: string;
  }
  
export interface RegisterResponse{
    activationKey: string;
    keyValidDuration: string;

}
export interface LoginRequest {
    login: string;
    email?: string;
    password : string
    phone?: string;
}

export interface LoginResponse {
    accessToken: string
}
