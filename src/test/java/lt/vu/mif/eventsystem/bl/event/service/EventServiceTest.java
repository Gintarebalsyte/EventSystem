package lt.vu.mif.eventsystem.bl.event.service;

import lt.vu.mif.eventsystem.bl.event.repository.EventRepository;
import lt.vu.mif.eventsystem.model.event.entity.Event;
import lt.vu.mif.eventsystem.model.event.enums.EventStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {
    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    @Test
    @DisplayName("Should throw an exception when the event is not active")
    void retrieveEventForOrderWhenEventIsNotActiveThenThrowException() {
        Event event = Event.builder()
                .status(EventStatus.INACTIVE)
                .build();

        Long id = 1L;

        when(eventRepository.findById(id)).thenReturn(Optional.of(event));

        assertThrows(
                IllegalArgumentException.class,
                () -> eventService.retrieveEventForOrder(id),
                "Event must be active");
    }

    @Test
    @DisplayName("Should return event when it is valid")
    void retrieveEventForOrderWhenValidThenReturnEvent() {
        Event event = Event.builder()
                .status(EventStatus.ACTIVE)
                .build();

        Long id = 1L;

        when(eventRepository.findById(id)).thenReturn(Optional.of(event));

        Event returnedValue = eventService.retrieveEventForOrder(id);

        assertEquals(event, returnedValue);
    }
}