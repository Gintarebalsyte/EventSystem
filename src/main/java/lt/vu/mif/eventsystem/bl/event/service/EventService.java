package lt.vu.mif.eventsystem.bl.event.service;

import lombok.RequiredArgsConstructor;
import lt.vu.mif.eventsystem.bl.event.repository.interfaces.IEventRepository;
import lt.vu.mif.eventsystem.bl.event.service.interfaces.IEventService;
import lt.vu.mif.eventsystem.bl.utils.ValidationUtils;
import lt.vu.mif.eventsystem.model.event.entity.Event;
import lt.vu.mif.eventsystem.model.event.enums.EventStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventService implements IEventService {
    private final IEventRepository eventRepository;

    @Override
    public Event retrieveEventForOrder(Long id) {
        Event event = eventRepository.findById(id).orElseThrow();
        ValidationUtils.validate(event.getStatus() == EventStatus.ACTIVE, "Event must be active");
        return event;
    }
}
