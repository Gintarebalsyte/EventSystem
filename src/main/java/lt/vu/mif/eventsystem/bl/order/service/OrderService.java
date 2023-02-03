package lt.vu.mif.eventsystem.bl.order.service;

import lombok.RequiredArgsConstructor;
import lt.vu.mif.eventsystem.bl.event.service.EventOccurenceService;
import lt.vu.mif.eventsystem.bl.event.service.EventService;
import lt.vu.mif.eventsystem.bl.order.dto.OrderRequest;
import lt.vu.mif.eventsystem.bl.order.repository.OrderRepository;
import lt.vu.mif.eventsystem.model.event.entity.Event;
import lt.vu.mif.eventsystem.model.event.entity.EventOccurrence;
import lt.vu.mif.eventsystem.model.order.entity.Order;
import lt.vu.mif.eventsystem.model.order.enums.OrderStatus;
import lt.vu.mif.eventsystem.model.user.UserData;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final EventService eventService;
    private final EventOccurenceService eventOccurenceService;
    private final OrderRepository orderRepository;

    public void create(OrderRequest orderRequest, UserData recipient) {
        Event event = eventService.retrieveEventForOrder(orderRequest.getEventView().getId());

        EventOccurrence eventOccurrence = eventOccurenceService.retrieveEventOccurrenceForOrder(orderRequest.getEventView().getOccurrenceId(), orderRequest.getNumberOfParticipants());

        Order order = Order.builder()
                .status(OrderStatus.SUBMITTED)
                .event(event)
                .recipient(recipient)
                .eventOccurrence(eventOccurrence)
                .additionalInformation(orderRequest.getAdditionalInformation())
                .build();

        orderRepository.save(order);

        eventOccurenceService.updateAvailableSeatsCount(eventOccurrence.getId(), -(orderRequest.getNumberOfParticipants()));
    }
}
