package my.ddos.mapper;


import my.ddos.event.EventChangedRole;

public class EventChangedRoleMapper {

    public EventChangedRole toEventChangedRole(String username, String changedBy, String role){
        return new EventChangedRole(username, changedBy, role);
    }
}
