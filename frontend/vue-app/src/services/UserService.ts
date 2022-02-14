import { LoginRequest, LoginResponse } from "../types/auth.module"

export const userService = {
    login,
    logout,
    register,
    update,
    activate,
};

function login(request: LoginRequest) {
    const requestOptions = {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body:JSON.stringify( request )
    };

}

function logout() {
    // remove user from local storage to log user out
    localStorage.removeItem('user');
}

function register(request:SignupRequest) {
    const requestOptions = {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(request)
    };

}


function activate(code:string,){ }


function getById(id) {
    const requestOptions = {
        method: 'GET',
        headers: authHeader()
    };

    return fetch(`${config.apiUrl}/users/${id}`, requestOptions).then(handleResponse);
}

function update(user) {
    const requestOptions = {
        method: 'PUT',
        headers: { ...authHeader(), 'Content-Type': 'application/json' },
        body: JSON.stringify(user)
    };

    return fetch(`${config.apiUrl}/users/${user.id}`, requestOptions).then(handleResponse);
}
