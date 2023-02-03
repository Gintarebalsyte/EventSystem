package lt.vu.mif.eventsystem.bl.event.service.interfaces;

import lt.vu.mif.eventsystem.model.event.entity.Event;

public interface IEventService {
    Event retrieveEventForOrder(Long id);
}
