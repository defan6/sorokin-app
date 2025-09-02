package my.ddos.mapper;


import my.ddos.event.EventChangedRole;
import org.springframework.stereotype.Component;

@Component
public class EventChangedRoleMapper {

    public EventChangedRole toEventChangedRole(String username, String changedBy, String role){
        return new EventChangedRole(username, changedBy, role);
    }
}
