package lt.vu.mif.eventsystem.bl.order.service;

import lt.vu.mif.eventsystem.bl.constants.AppConstants;
import lt.vu.mif.eventsystem.bl.event.service.EventOccurrenceService;
import lt.vu.mif.eventsystem.bl.event.service.EventService;
import lt.vu.mif.eventsystem.bl.order.dto.OrderRequest;
import lt.vu.mif.eventsystem.bl.order.repository.OrderRepository;
import lt.vu.mif.eventsystem.model.event.entity.Event;
import lt.vu.mif.eventsystem.model.event.entity.EventOccurrence;
import lt.vu.mif.eventsystem.model.order.entity.Order;
import lt.vu.mif.eventsystem.model.order.enums.OrderStatus;
import lt.vu.mif.eventsystem.model.user.UserData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private EventService eventService;

    @Mock
    private EventOccurrenceService eventOccurrenceService;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Captor
    ArgumentCaptor<Order> orderArgumentCaptor;

    @Test
    @DisplayName("Should throw an exception when the event is not valid")
    void createWhenEventIsNotValidThenThrowException() {
        when(eventService.retrieveEventForOrder(anyLong())).thenThrow(IllegalArgumentException.class);

        assertThrows(
                IllegalArgumentException.class,
                () -> orderService.create(mock(OrderRequest.class), mock(UserData.class)));

        verify(orderRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("Should throw an exception when the event occurrence is not valid")
    void createWhenEventOccurrenceIsNotValidThenThrowException() {
        when(eventService.retrieveEventForOrder(anyLong())).thenReturn(mock(Event.class));
        when(eventOccurrenceService.retrieveEventOccurrenceForOrder(anyLong(), any(), anyInt()))
                .thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> orderService.create(mock(OrderRequest.class), mock(UserData.class)));

        verify(orderRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("Should save the order and update the available seats count when everything is ok")
    void createWhenEverythingIsOkThenSaveOrderAndUpdateAvailableSeatsCount() {
        LocalDateTime dateTime = LocalDateTime.parse("2023-01-01 12:00", AppConstants.LOCAL_DATE_TIME_FORMATTER);

        OrderRequest orderRequest = OrderRequest.builder()
                .eventId(1L)
                .eventOccurrenceId(1L)
                .additionalInformation("Additional information")
                .numberOfParticipants(2)
                .createDate(dateTime)
                .build();

        Event event = new Event();
        EventOccurrence eventOccurrence = new EventOccurrence();
        UserData recipient = new UserData();

        Order order = Order.builder()
                .status(OrderStatus.SUBMITTED)
                .event(event)
                .recipient(recipient)
                .eventOccurrence(eventOccurrence)
                .additionalInformation(orderRequest.getAdditionalInformation())
                .build();

        when(eventService.retrieveEventForOrder(orderRequest.getEventId())).
                thenReturn(event);
        when(eventOccurrenceService.retrieveEventOccurrenceForOrder(orderRequest.getEventOccurrenceId(), orderRequest.getCreateDate(), orderRequest.getNumberOfParticipants()))
                .thenReturn(eventOccurrence);

        orderService.create(orderRequest, recipient);

        verify(orderRepository).save(orderArgumentCaptor.capture());
        assertTrue(sameOrder(order, orderArgumentCaptor.getValue()));

        verify(eventOccurrenceService).updateAvailableSeatsCount(eventOccurrence.getId(), -orderRequest.getNumberOfParticipants());
    }

    boolean sameOrder(Order o1, Order o2) {
        if (o1 == o2) return true;
        return o1.getStatus() == o2.getStatus()
                && Objects.equals(o1.getEvent(), o2.getEvent())
                && Objects.equals(o1.getEventOccurrence(), o2.getEventOccurrence())
                && Objects.equals(o1.getRecipient(), o2.getRecipient())
                && Objects.equals(o1.getAdditionalInformation(), o2.getAdditionalInformation());
    }
}