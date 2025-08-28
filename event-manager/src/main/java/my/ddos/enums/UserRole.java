package my.ddos.enums;

import my.ddos.exception.InvalidUserRoleException;

public enum UserRole {
    ROLE_USER, ROLE_ADMIN;


    public static UserRole fromString(String role){
        for(UserRole userRole: UserRole.values()){
            if(userRole.name().equalsIgnoreCase(role)){
                return userRole;
            }
        }
        throw new InvalidUserRoleException(role);
    }
}
