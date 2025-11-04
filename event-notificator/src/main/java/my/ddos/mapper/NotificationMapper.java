package my.ddos.mapper;

import my.ddos.model.dto.NotificationResponse;
import my.ddos.model.entity.Notification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {


    NotificationResponse toResponse(Notification entity);
}
