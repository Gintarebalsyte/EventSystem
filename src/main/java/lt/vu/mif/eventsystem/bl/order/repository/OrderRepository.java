package lt.vu.mif.eventsystem.bl.order.repository;

import lt.vu.mif.eventsystem.model.order.entity.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {
}
