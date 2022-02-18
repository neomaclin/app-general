 
export interface User{
    login: string;
    email: string;
    role: string;
}

export interface RegisterRequest {
    login: string;
    email: string;
    password : string
    phone: string | null;
  }
  
export interface RegisterResponse{
    activationKey: string;
    keyValidDuration: string;
}

export interface LoginRequest {
    login: string | null;
    email: string | null;
    password : string
    phone: string | null;
}

export interface LoginResponse {
    accessToken: string
}
