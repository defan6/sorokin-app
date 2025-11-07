import {useEffect, useState} from "react";
import AuthService from "../services/AuthService";


interface User {
    accessToken: string
    username: string
    fullName: string
}


const ProfilePage: React.FC = () => {
    const [currentUser, setCurrentUser] = useState<User | null>(null);


    useEffect(() => {
        const user = AuthService.getCurrentUser();
        if(user){
            setCurrentUser(user);
        }
    }, []);


    return  (
        <div>
            <header className="jumbotron">
                <h3>
                    <strong>{currentUser?.username}Profile</strong>
                </h3>
            </header>
            {currentUser ? (
                <div>
                    <p>
                        <strong>Token:</strong> {currentUser.accessToken.substring(0, 20)}...

                    </p>
                </div>
            ) : (
                <p>No user is logged in. </p>
            )}
        </div>
    );
};


export default ProfilePage


