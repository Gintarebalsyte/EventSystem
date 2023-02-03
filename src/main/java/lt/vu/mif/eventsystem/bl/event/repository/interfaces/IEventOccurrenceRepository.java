package lt.vu.mif.eventsystem.bl.event.repository.interfaces;

import lt.vu.mif.eventsystem.model.event.entity.EventOccurrence;
import org.springframework.data.repository.CrudRepository;

public interface IEventOccurrenceRepository extends CrudRepository<EventOccurrence, Long> {
}
