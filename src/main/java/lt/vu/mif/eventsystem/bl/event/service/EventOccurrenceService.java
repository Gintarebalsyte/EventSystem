package lt.vu.mif.eventsystem.bl.event.service;

import lombok.RequiredArgsConstructor;
import lt.vu.mif.eventsystem.bl.event.repository.interfaces.IEventOccurrenceRepository;
import lt.vu.mif.eventsystem.bl.event.service.interfaces.IEventOccurrenceService;
import lt.vu.mif.eventsystem.bl.utils.ValidationUtils;
import lt.vu.mif.eventsystem.model.event.entity.EventOccurrence;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EventOccurrenceService implements IEventOccurrenceService {
    private final IEventOccurrenceRepository eventOccurrenceRepository;

    @Override
    public EventOccurrence retrieveEventOccurrenceForOrder(Long id, LocalDateTime deadline, int numberOfParticipants) {
        EventOccurrence eventOccurrence = eventOccurrenceRepository.findById(id).orElseThrow();
        ValidationUtils.validate(eventOccurrence.getEventDate().isAfter(deadline), "Can't place orders for events that have already started or ended");
        ValidationUtils.validate(numberOfParticipants <= eventOccurrence.getAvailableSeatsCount(), "Not enough available seats");
        return eventOccurrence;
    }

    @Override
    public void updateAvailableSeatsCount(Long id, int parameter) {
        EventOccurrence eventOccurrence = eventOccurrenceRepository.findById(id).orElseThrow();
        eventOccurrence.setAvailableSeatsCount(eventOccurrence.getAvailableSeatsCount() + parameter);
        eventOccurrenceRepository.save(eventOccurrence);
    }
}
