package ru.brombin.assistant_ai_outbox_processor.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "incidents_outbox")
@FieldDefaults(level = PRIVATE)
public class IncidentOutboxEvent {

    @Id
    @GeneratedValue
    UUID id;

    @Column(nullable = false)
    String aggregateType;

    @Column(nullable = false)
    String aggregateId;

    @Column(nullable = false)
    String type;

    @Lob
    @Column(nullable = false)
    String payload;

    @Column(nullable = false)
    LocalDateTime createdAt;

    @Column(nullable = false)
    boolean processed = false;

    @Column(nullable = true)
    LocalDateTime processedAt;
}
