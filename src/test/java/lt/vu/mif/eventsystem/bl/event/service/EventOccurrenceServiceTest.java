package lt.vu.mif.eventsystem.bl.event.service;

import lt.vu.mif.eventsystem.bl.constants.AppConstants;
import lt.vu.mif.eventsystem.bl.event.repository.EventOccurrenceRepository;
import lt.vu.mif.eventsystem.model.event.entity.EventOccurrence;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventOccurrenceServiceTest {
    @Mock
    private EventOccurrenceRepository eventOccurrenceRepository;

    @InjectMocks
    private EventOccurrenceService eventOccurrenceService;

    @Captor
    ArgumentCaptor<EventOccurrence> eventOccurrenceArgumentCaptor;

    @Test
    @DisplayName(
            "Should throw an exception when the number of participants is greater than the available seats count")
    void retrieveEventOccurrenceForOrderWhenNumberOfParticipantsIsGreaterThanAvailableSeatsCountThenThrowException() {
        LocalDateTime dateTime = LocalDateTime.parse("2023-01-01 12:00", AppConstants.LOCAL_DATE_TIME_FORMATTER);
        int seatCount = 10;
        Long id = 1L;

        EventOccurrence eventOccurrence = EventOccurrence.builder()
                .eventDate(dateTime.plusDays(1))
                .availableSeatsCount(seatCount)
                .build();

        when(eventOccurrenceRepository.findById(id)).thenReturn(Optional.of(eventOccurrence));

        assertThrows(
                IllegalArgumentException.class,
                () -> eventOccurrenceService.retrieveEventOccurrenceForOrder(id, dateTime, seatCount + 1),
                "Not enough available seats");
    }

    @Test
    @DisplayName("Should throw an exception when the event has already started or ended")
    void retrieveEventOccurrenceForOrderWhenEventHasAlreadyStartedOrEndedThenThrowException() {
        LocalDateTime dateTime = LocalDateTime.parse("2023-01-01 12:00", AppConstants.LOCAL_DATE_TIME_FORMATTER);
        int seatCount = 10;
        Long id = 1L;

        EventOccurrence eventOccurrence = EventOccurrence.builder()
                .eventDate(dateTime)
                .availableSeatsCount(seatCount)
                .build();
        when(eventOccurrenceRepository.findById(id)).thenReturn(Optional.of(eventOccurrence));

        assertThrows(
                IllegalArgumentException.class,
                () -> eventOccurrenceService.retrieveEventOccurrenceForOrder(id, dateTime.plusDays(1L), seatCount),
                "Can't place orders for events that have already started or ended");
    }

    @Test
    @DisplayName("Should return event occurrence when it is valid")
    void retrieveEventOccurrenceForOrderWhenValidThenReturnEventOccurrence() {
        LocalDateTime dateTime = LocalDateTime.parse("2023-01-01 12:00", AppConstants.LOCAL_DATE_TIME_FORMATTER);
        int seatCount = 10;
        Long id = 1L;

        EventOccurrence eventOccurrence = EventOccurrence.builder()
                .eventDate(dateTime.plusDays(1L))
                .availableSeatsCount(seatCount)
                .build();
        when(eventOccurrenceRepository.findById(id))
                .thenReturn(Optional.of(eventOccurrence));

        EventOccurrence returnedValue = eventOccurrenceService.retrieveEventOccurrenceForOrder(id, dateTime, seatCount);

        assertEquals(eventOccurrence, returnedValue);
    }

    @Test
    @DisplayName("Should update the available seats count")
    void updateAvailableSeatsCountWhenEventOccurrenceExists() {
        Long id = 1L;
        int seatCount = 10;
        int parameter = 5;

        EventOccurrence eventOccurrence = EventOccurrence.builder()
                .id(id)
                .availableSeatsCount(seatCount)
                .build();

        when(eventOccurrenceRepository.findById(id)).thenReturn(Optional.of(eventOccurrence));

        eventOccurrenceService.updateAvailableSeatsCount(id, -parameter);

        verify(eventOccurrenceRepository).save(eventOccurrenceArgumentCaptor.capture());

        assertEquals(seatCount - parameter, eventOccurrenceArgumentCaptor.getValue().getAvailableSeatsCount());
    }
}