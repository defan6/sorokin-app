import api from './api';

const API_URL = '/api/auth';

// Типы данных на основе вашего бэкенда
interface LoginRequest {
  username?: string;
  password?: string;
}

interface LoginResponse {
  accessToken: string;
  username: string;
  fullName: string;
  roles: string[]
}

interface RegisterRequest {
  username?: string;
  fullName?: string;
  password?: string;
}

interface RegisterResponse {
  message: string;
}

class AuthService {
  login(request: LoginRequest) {
    return api.post<LoginResponse>(API_URL + '/login', request).then(response => {
      if (response.data.accessToken) {
        localStorage.setItem('user', JSON.stringify(response.data));
      }
      return response;
    });
  }

  logout() {
    localStorage.removeItem('user');
  }

  registerUser(request: RegisterRequest) {
    return api.post<RegisterResponse>(API_URL + '/register/user', request);
  }

  getCurrentUser() {
    const userStr = localStorage.getItem('user');
    if (userStr) {
      return JSON.parse(userStr) as LoginResponse;
    }
    return null;
  }
}

const authService = new AuthService();
export default authService;