import api from './api';

const API_URL = '/api/manager/users';

export interface Role {
    id: number;
    role: string;
}

// Этот интерфейс должен соответствовать UserResponse из бэкенда
export interface User {
    id: number;
    username: string;
    fullName: string;
    userRoles: Role[];
}

export interface ChangeRoleRequest {
    id: number;
    role: string;
}

class UserService {

    getAllUsers() {
        return api.get<User[]>(`${API_URL}/admin`);
    }

    changeRole(data: ChangeRoleRequest) {
        return api.patch<User>(`${API_URL}/admin/change-role`, data);
    }

    // Метод getInfoAboutCurrentUser может быть здесь, если потребуется
    // getInfoAboutCurrentUser() {
    //     return api.get<User>(`${API_URL}/me`);
    // }
}

export default new UserService();
