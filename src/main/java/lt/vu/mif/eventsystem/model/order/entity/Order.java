package lt.vu.mif.eventsystem.model.order.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lt.vu.mif.eventsystem.model.event.entity.Event;
import lt.vu.mif.eventsystem.model.event.entity.EventOccurrence;
import lt.vu.mif.eventsystem.model.order.enums.OrderStatus;
import lt.vu.mif.eventsystem.model.user.UserData;

@Setter
@Getter
@Entity
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "order")
public class Order {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_occurrence_id", nullable = false)
    private EventOccurrence eventOccurrence;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false)
    private UserData recipient;

    @Column(name = "additional_information")
    private String additionalInformation;
}
