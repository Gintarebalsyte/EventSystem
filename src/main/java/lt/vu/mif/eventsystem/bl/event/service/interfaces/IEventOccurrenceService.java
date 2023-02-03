package lt.vu.mif.eventsystem.bl.event.service.interfaces;

import lt.vu.mif.eventsystem.model.event.entity.EventOccurrence;

import java.time.LocalDateTime;

public interface IEventOccurrenceService {
    EventOccurrence retrieveEventOccurrenceForOrder(Long id, LocalDateTime deadline, int numberOfParticipants);
    void updateAvailableSeatsCount(Long id, int parameter);
}
