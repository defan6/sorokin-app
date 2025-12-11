import api from './api';

const EVENT_MANAGER_URL = '/api/manager/users';
const USER_PROFILE_URL = '/api/profiles'; // Assuming this is the base for user profile operations

export interface Role {
    id: number;
    role: string;
}

// Updated interface to include profilePhotoUrl
export interface User {
    id: number;
    username: string;
    fullName: string;
    userRoles: Role[];
    avatarUrl: string | null; // Added profile photo URL field
}


export interface Profile {
    userId: number;
    username: string;
    fullName: string;
    bio: string;
    address: string;
    avatarUrl: string;
}

export interface ChangeRoleRequest {
    id: number;
    role: string;
}

class UserService {

    async getAllUsers(): Promise<User[]> {
        try {
            const [response1, response2] = await Promise.all([
                api.get<User[]>(`${EVENT_MANAGER_URL}/admin`),
                api.get<Profile[]>(`${USER_PROFILE_URL}`)
            ]);

            const avatarMap = new Map<number, string>();
            response2.data.forEach(profile => {
                avatarMap.set(profile.userId, profile.avatarUrl);
            });

            const combinedUsers: User[] = response1.data.map(user => ({
                ...user,
                avatarUrl: avatarMap.get(user.id) || null
            }));

            return combinedUsers;
        } catch (err) {
            console.error("Failed to fetch users from both services", err);
            throw err;
        }
    }



    changeRole(data: ChangeRoleRequest) {
        return api.patch<User>(`${EVENT_MANAGER_URL}/admin/change-role`, data);
    }

    // Method to get detailed user profile including photo URL
    getUserProfile(): Promise<{ data: User }> {
        // Assuming /api/users/me fetches the currently authenticated user's full profile
        return api.get<User>(`${USER_PROFILE_URL}/me`);
    }

    // Method to update the user's profile photo URL
    updateUserProfilePhoto(userId: number, photoUrl: string): Promise<{ data: User }> {
        // Assuming /api/users/{userId}/photo is the endpoint to update the photo URL
        return api.patch<User>(`${USER_PROFILE_URL}/${userId}/photo`, { profilePhotoUrl: photoUrl });
    }

    // Original commented out method, can be removed or adapted if getUserProfile serves the same purpose
    // getInfoAboutCurrentUser() {
    //     return api.get<User>(`${API_URL}/me`);
    // }
}

const userService = new UserService();
export default userService;
