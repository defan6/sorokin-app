import {useEffect, useState} from "react";
import userService, {User as UserServiceUser} from "../services/UserService"; // Import User interface from UserService

// Extend the local User interface to match the one from UserService, or just use UserServiceUser directly
interface User extends UserServiceUser {
    // accessToken and other fields from AuthService.getCurrentUser() might still be relevant
    // but we'll prioritize fetching the full profile from UserService
    // For now, let's assume we might still need accessToken for some display purposes or rely solely on userServiceUser structure.
    // If AuthService.getCurrentUser() returns a different structure, we'll need to map it.
    // For simplicity, let's align the state with UserServiceUser structure.
}


const ProfilePage: React.FC = () => {
    // Use the User type from UserService for state
    const [currentUser, setCurrentUser] = useState<UserServiceUser | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchProfile = async () => {
            try {
                setLoading(true);
                // Fetch the full user profile including photo URL
                const response = await userService.getUserProfile();
                setCurrentUser(response.data);
                console.log("Photo url: ", response.data.avatarUrl);
                setError(null);
            } catch (err: any) {
                console.error("Error fetching user profile:", err);
                setError("Failed to load profile data.");
            } finally {
                setLoading(false);
            }
        };

        fetchProfile();
    }, []);

    return  (
        <div className="container mt-4">
            <header className="mb-4">
                <h3>
                    <strong>Profile</strong>
                </h3>
            </header>
            {loading && <p>Loading profile...</p>}
            {error && <p className="text-danger">{error}</p>}
            {currentUser ? (
                <div className="card p-3">
                    <div className="row align-items-center">
                        <div className="col-md-3 text-center">
                            {currentUser.avatarUrl ? (
                                <img 
                                    src={currentUser.avatarUrl}
                                    alt="Profile"
                                    className="img-fluid rounded-circle" 
                                    style={{ width: '150px', height: '150px', objectFit: 'cover' }}
                                />
                            ) : (
                                <div className="placeholder-profile-photo d-flex justify-content-center align-items-center rounded-circle bg-secondary text-white" style={{ width: '150px', height: '150px', fontSize: '3rem' }}>
                                    {currentUser.fullName?.charAt(0).toUpperCase() || currentUser.username?.charAt(0).toUpperCase()}
                                </div>
                            )}
                        </div>
                        <div className="col-md-9">
                            <h4><strong>{currentUser.fullName}</strong></h4>
                            <p><strong>Username:</strong> {currentUser.username}</p>
                            {/* Displaying roles if available */}
                            {currentUser.userRoles && currentUser.userRoles.length > 0 && (
                                <p><strong>Roles:</strong> {currentUser.userRoles.map(role => role.role).join(', ')}</p>
                            )}
                            {/* Token display is removed as we are fetching detailed user data */}
                            {/* <p><strong>Token:</strong> {currentUser.accessToken.substring(0, 20)}...</p> */}
                        </div>
                    </div>
                </div>
            ) : (!loading && !error && <p>No user is logged in.</p>) /* Show this only if not loading and no error */}
        </div>
    );
};


export default ProfilePage


