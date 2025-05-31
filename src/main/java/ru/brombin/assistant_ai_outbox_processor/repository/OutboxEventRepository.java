package ru.brombin.assistant_ai_outbox_processor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.brombin.assistant_ai_outbox_processor.entity.IncidentOutboxEvent;

import java.util.List;
import java.util.UUID;

@Repository
public interface OutboxEventRepository extends JpaRepository<IncidentOutboxEvent, UUID> {
    List<IncidentOutboxEvent> findAllByProcessedFalseOrderByCreatedAtAsc();
}
