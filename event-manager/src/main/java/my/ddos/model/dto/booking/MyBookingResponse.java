package my.ddos.model.dto.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyBookingResponse {
    private Long id;
    private String eventName;
    private LocalDateTime eventDate;
    private String venueName;
    private String status;
}
