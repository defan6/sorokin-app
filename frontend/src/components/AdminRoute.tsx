import AuthService from "../services/AuthService";
import {Navigate, Outlet} from "react-router-dom";


const AdminRoute: React.FC = () => {
    const currentUser = AuthService.getCurrentUser();

    const isAdmin = currentUser?.roles?.includes('ROLE_ADMIN');


    return isAdmin ? <Outlet/> : <Navigate to="/" />

}


export default AdminRoute;