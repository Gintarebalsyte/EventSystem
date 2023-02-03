package lt.vu.mif.eventsystem.bl.order.dto;

import lombok.*;
import lt.vu.mif.eventsystem.bl.event.dto.EventView;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class OrderRequest {
    private EventView eventView;
    private String additionalInformation;
    private int numberOfParticipants;
}
