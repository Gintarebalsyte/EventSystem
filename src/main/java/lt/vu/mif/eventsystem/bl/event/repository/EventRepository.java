package lt.vu.mif.eventsystem.bl.event.repository;

import lt.vu.mif.eventsystem.model.event.entity.Event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends CrudRepository<Event, Long> {
}
