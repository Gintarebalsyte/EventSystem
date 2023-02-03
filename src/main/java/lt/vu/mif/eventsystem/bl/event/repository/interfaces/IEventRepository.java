package lt.vu.mif.eventsystem.bl.event.repository.interfaces;

import lt.vu.mif.eventsystem.model.event.entity.Event;
import org.springframework.data.repository.CrudRepository;

public interface IEventRepository extends CrudRepository<Event, Long> {
}

