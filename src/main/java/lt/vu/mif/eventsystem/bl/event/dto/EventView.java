package lt.vu.mif.eventsystem.bl.event.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EventView {
    private Long id;
    private Long occurrenceId;
}
