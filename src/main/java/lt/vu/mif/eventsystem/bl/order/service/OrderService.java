package lt.vu.mif.eventsystem.bl.order.service;

import lombok.RequiredArgsConstructor;
import lt.vu.mif.eventsystem.bl.event.service.interfaces.IEventOccurrenceService;
import lt.vu.mif.eventsystem.bl.event.service.interfaces.IEventService;
import lt.vu.mif.eventsystem.bl.order.dto.OrderRequest;
import lt.vu.mif.eventsystem.bl.order.repository.interfaces.IOrderRepository;
import lt.vu.mif.eventsystem.model.event.entity.Event;
import lt.vu.mif.eventsystem.model.event.entity.EventOccurrence;
import lt.vu.mif.eventsystem.model.order.entity.Order;
import lt.vu.mif.eventsystem.model.order.enums.OrderStatus;
import lt.vu.mif.eventsystem.model.user.UserData;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final IEventService eventService;
    private final IEventOccurrenceService eventOccurrenceService;
    private final IOrderRepository orderRepository;

    public void create(OrderRequest orderRequest, UserData recipient) {
        Event event = eventService.retrieveEventForOrder(orderRequest.getEventId());
        EventOccurrence eventOccurrence = eventOccurrenceService.retrieveEventOccurrenceForOrder(orderRequest.getEventOccurrenceId(), orderRequest.getCreateDate(), orderRequest.getNumberOfParticipants());

        Order order = Order.builder()
                .status(OrderStatus.SUBMITTED)
                .event(event)
                .recipient(recipient)
                .eventOccurrence(eventOccurrence)
                .additionalInformation(orderRequest.getAdditionalInformation())
                .build();

        orderRepository.save(order);

        eventOccurrenceService.updateAvailableSeatsCount(eventOccurrence.getId(), -(orderRequest.getNumberOfParticipants()));
    }
}
