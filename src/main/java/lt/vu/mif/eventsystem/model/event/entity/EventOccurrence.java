package lt.vu.mif.eventsystem.model.event.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "event_occurrence")
public class EventOccurrence {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @Column(name = "event_date")
    private int availableSeatsCount;

    // Additional EventOccurrence data omitted for the sake of simplicity
}
