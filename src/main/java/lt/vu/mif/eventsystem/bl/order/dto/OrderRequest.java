package lt.vu.mif.eventsystem.bl.order.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class OrderRequest {
    private Long eventId;
    private Long eventOccurrenceId;
    private String additionalInformation;
    private int numberOfParticipants;
    private LocalDateTime createDate;
}
