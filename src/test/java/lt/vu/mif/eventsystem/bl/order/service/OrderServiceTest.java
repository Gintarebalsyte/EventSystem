package lt.vu.mif.eventsystem.bl.order.service;

import lt.vu.mif.eventsystem.bl.event.dto.EventView;
import lt.vu.mif.eventsystem.bl.event.repository.EventOccurrenceRepository;
import lt.vu.mif.eventsystem.bl.event.repository.EventRepository;
import lt.vu.mif.eventsystem.bl.order.dto.OrderRequest;
import lt.vu.mif.eventsystem.bl.order.repository.OrderRepository;
import lt.vu.mif.eventsystem.bl.user.repository.UserDataRepository;
import lt.vu.mif.eventsystem.bl.utils.AuthenticationUtils;
import lt.vu.mif.eventsystem.model.event.entity.Event;
import lt.vu.mif.eventsystem.model.event.entity.EventOccurrence;
import lt.vu.mif.eventsystem.model.event.enums.EventStatus;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private EventRepository eventRepository;
    @Mock
    private EventOccurrenceRepository eventOccurrenceRepository;
    @Mock
    private UserDataRepository userDataRepository;
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Captor
    ArgumentCaptor<Order> orderCaptor;

    @Captor
    ArgumentCaptor<EventOccurrence> eventOccurrenceCaptor;

    @Test
    @DisplayName("Should throw an exception when the event is not active")
    void createWhenEventIsNotActiveThenThrowException() {
        EventView eventView = EventView.builder()
                .id(1L)
                .build();

        Event event = Event.builder()
                .status(EventStatus.INACTIVE)
                .build();

        OrderRequest orderRequest = OrderRequest.builder()
                .eventView(eventView)
                .build();

        when(eventRepository.findById(orderRequest.getEventView().getId()))
                .thenReturn(Optional.of(event));

        assertThrows(
                IllegalArgumentException.class,
                () -> orderService.create(orderRequest),
                "Event must be active");

        verify(orderRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("Should throw an exception when the event has already started or ended")
    void createWhenEventHasAlreadyStartedOrEndedThenThrowException() {
        int availableSeats = 10;
        Event event = Event.builder()
                .status(EventStatus.ACTIVE)
                .build();

        EventOccurrence eventOccurrence = EventOccurrence.builder()
                .eventDate(LocalDateTime.now().minusDays(1))
                .availableSeatsCount(availableSeats)
                .build();

        EventView eventView = EventView.builder()
                .id(1L)
                .occurrenceId(1L)
                .build();

        OrderRequest orderRequest = OrderRequest.builder()
                .eventView(eventView)
                .numberOfParticipants(availableSeats - 1)
                .build();

        when(eventRepository.findById(orderRequest.getEventView().getId()))
                .thenReturn(Optional.of(event));
        when(eventOccurrenceRepository.findById(orderRequest.getEventView().getOccurrenceId()))
                .thenReturn(Optional.of(eventOccurrence));

        assertThrows(IllegalArgumentException.class,
                () -> orderService.create(orderRequest),
                "Can't place orders for events that have already started or ended");
    }

    @Test
    @DisplayName("Should throw an exception when there are not enough available seats")
    void createWhenThereAreNotEnoughAvailableSeatsThenThrowException() {
        int seatCount = 1;

        EventView eventView = EventView.builder()
                .id(1L)
                .occurrenceId(1L)
                .build();

        OrderRequest orderRequest = OrderRequest.builder()
                .eventView(eventView)
                .numberOfParticipants(seatCount + 1)
                .build();

        Event event = Event.builder()
                .id(orderRequest.getEventView().getId())
                .status(EventStatus.ACTIVE)
                .build();

        EventOccurrence eventOccurrence = EventOccurrence.builder()
                .id(orderRequest.getEventView().getOccurrenceId())
                .eventDate(LocalDateTime.now().plusDays(1))
                .availableSeatsCount(seatCount)
                .build();

        when(eventRepository.findById(orderRequest.getEventView().getId()))
                .thenReturn(Optional.of(event));
        when(eventOccurrenceRepository.findById(orderRequest.getEventView().getOccurrenceId()))
                .thenReturn(Optional.of(eventOccurrence));

        assertThrows(IllegalArgumentException.class,
                () -> orderService.create(orderRequest),
                "Not enough available seats");

        verify(orderRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("Should save the order when everything is ok")
    void createWhenEverythingIsOkThenSaveOrder() {
        int availableSeats = 10;

        Event event = Event.builder()
                .id(1L)
                .status(EventStatus.ACTIVE)
                .build();

        EventOccurrence eventOccurrence = EventOccurrence.builder()
                .id(1L)
                .eventDate(LocalDateTime.now().plusDays(1))
                .availableSeatsCount(availableSeats)
                .build();

        UserData recipient = UserData.builder()
                .id(0L)
                .build();

        EventView eventView = EventView.builder()
                .id(1L)
                .occurrenceId(1L)
                .build();

        OrderRequest orderRequest = OrderRequest.builder()
                .eventView(eventView)
                .numberOfParticipants(availableSeats - 1)
                .additionalInformation("additional information")
                .build();

        Order order = Order.builder()
                .status(OrderStatus.SUBMITTED)
                .event(event)
                .eventOccurrence(eventOccurrence)
                .recipient(recipient)
                .additionalInformation(orderRequest.getAdditionalInformation())
                .build();

        when(eventRepository.findById(orderRequest.getEventView().getId()))
                .thenReturn(Optional.of(event));
        when(eventOccurrenceRepository.findById(orderRequest.getEventView().getOccurrenceId()))
                .thenReturn(Optional.of(eventOccurrence));
        when(userDataRepository.findById(AuthenticationUtils.getUserId()))
                .thenReturn(Optional.of(recipient));

        orderService.create(orderRequest);

        verify(orderRepository).save(orderCaptor.capture());
        assertTrue(sameOrder(order, orderCaptor.getValue()));
    }

    @Test
    @DisplayName("Should save updated event occurrence when everything is ok")
    void createWhenEverythingIsOkThenSaveUpdatedEventOccurrence() {
        int availableSeats = 10;
        int numberOfParticipants = 5;

        Event event = Event.builder()
                .id(1L)
                .status(EventStatus.ACTIVE)
                .build();

        EventOccurrence eventOccurrence = EventOccurrence.builder()
                .id(1L)
                .eventDate(LocalDateTime.now().plusDays(1))
                .availableSeatsCount(availableSeats)
                .build();

        UserData recipient = UserData.builder()
                .id(0L)
                .build();

        EventView eventView = EventView.builder()
                .id(1L)
                .occurrenceId(1L)
                .build();

        OrderRequest orderRequest = OrderRequest.builder()
                .eventView(eventView)
                .numberOfParticipants(numberOfParticipants)
                .additionalInformation("additional information")
                .build();

        when(eventRepository.findById(orderRequest.getEventView().getId()))
                .thenReturn(Optional.of(event));
        when(eventOccurrenceRepository.findById(orderRequest.getEventView().getOccurrenceId()))
                .thenReturn(Optional.of(eventOccurrence));
        when(userDataRepository.findById(AuthenticationUtils.getUserId()))
                .thenReturn(Optional.of(recipient));

        orderService.create(orderRequest);

        verify(orderRepository).save(any());
        verify(eventOccurrenceRepository).save(eventOccurrenceCaptor.capture());
        assertEquals(availableSeats - numberOfParticipants, eventOccurrenceCaptor.getValue().getAvailableSeatsCount());
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