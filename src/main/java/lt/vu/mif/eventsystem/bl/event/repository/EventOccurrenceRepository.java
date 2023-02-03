package lt.vu.mif.eventsystem.bl.event.repository;

import lt.vu.mif.eventsystem.model.event.entity.EventOccurrence;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventOccurrenceRepository extends CrudRepository<EventOccurrence, Long> {
}
