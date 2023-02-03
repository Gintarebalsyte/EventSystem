package lt.vu.mif.eventsystem.bl.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import lt.vu.mif.eventsystem.bl.event.repository.EventOccurrenceRepository;
import lt.vu.mif.eventsystem.bl.event.repository.EventRepository;
import lt.vu.mif.eventsystem.bl.order.dto.OrderRequest;
import lt.vu.mif.eventsystem.bl.order.repository.OrderRepository;
import lt.vu.mif.eventsystem.bl.user.repository.UserDataRepository;
import lt.vu.mif.eventsystem.bl.utils.AuthenticationUtils;
import lt.vu.mif.eventsystem.bl.utils.ValidationUtils;
import lt.vu.mif.eventsystem.model.event.entity.Event;
import lt.vu.mif.eventsystem.model.event.entity.EventOccurrence;
import lt.vu.mif.eventsystem.model.event.enums.EventStatus;
import lt.vu.mif.eventsystem.model.order.entity.Order;
import lt.vu.mif.eventsystem.model.order.enums.OrderStatus;
import lt.vu.mif.eventsystem.model.user.UserData;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@CommonsLog
@RequiredArgsConstructor
public class OrderService {
    private final EventRepository eventRepository;
    private final EventOccurrenceRepository eventOccurrenceRepository;
    private final UserDataRepository userDataRepository;
    private final OrderRepository orderRepository;

    public void create(OrderRequest orderRequest) {
        Event event = eventRepository.findById(orderRequest.getEventView().getId()).orElseThrow();
        ValidationUtils.validate(event.getStatus() == EventStatus.ACTIVE, "Event must be active");

        EventOccurrence eventOccurrence = eventOccurrenceRepository.findById(orderRequest.getEventView().getOccurrenceId()).orElseThrow();
        ValidationUtils.validate(eventOccurrence.getEventDate().isAfter(LocalDateTime.now()), "Can't place orders for events that have already started or ended");
        ValidationUtils.validate(orderRequest.getNumberOfParticipants() <= eventOccurrence.getAvailableSeatsCount(), "Not enough available seats");

        UserData recipient = userDataRepository.findById(AuthenticationUtils.getUserId()).orElseThrow();

        Order order = Order.builder()
                .status(OrderStatus.SUBMITTED)
                .event(event)
                .recipient(recipient)
                .eventOccurrence(eventOccurrence)
                .additionalInformation(orderRequest.getAdditionalInformation())
                .build();

        orderRepository.save(order);

        eventOccurrence.setAvailableSeatsCount(eventOccurrence.getAvailableSeatsCount() - orderRequest.getNumberOfParticipants());
        eventOccurrenceRepository.save(eventOccurrence);
    }
}
