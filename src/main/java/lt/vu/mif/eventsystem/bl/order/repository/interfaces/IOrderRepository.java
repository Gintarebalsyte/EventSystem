package lt.vu.mif.eventsystem.bl.order.repository.interfaces;

import lt.vu.mif.eventsystem.model.order.entity.Order;
import org.springframework.data.repository.CrudRepository;

public interface IOrderRepository extends CrudRepository<Order, Long> {
}
