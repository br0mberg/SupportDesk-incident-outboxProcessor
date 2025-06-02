package ru.brombin.assistant_ai_outbox_processor.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.brombin.assistant_ai_outbox_processor.entity.IncidentOutboxEvent;
import ru.brombin.assistant_ai_outbox_processor.repository.OutboxEventRepository;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RabbitMqOutboxService {
    OutboxEventRepository outboxRepository;
    RabbitTemplate rabbitTemplate;

    @NonFinal
    @Value("${outbox.exchange}")
    String exchange;

    @NonFinal
    @Value("${outbox.routing-key}")
    String routingKey;

    @Transactional
    public void processEvent(IncidentOutboxEvent event) {
        try {
            // Синхронная отправка через invoke (ждёт подтверждения)
            rabbitTemplate.invoke(operations -> {
                operations.convertAndSend(exchange, routingKey, event.getPayload());
                return null;
            });

            // Отметка как обработанное только после успешной отправки
            event.setProcessed(true);
            event.setProcessedAt(LocalDateTime.now());
            outboxRepository.save(event);

            log.info("Outbox event sent: {}", event.getId());

        } catch (Exception e) {
            log.error("Failed to send outbox event: {}", event.getId(), e);
            throw e; // чтобы транзакция откатилась
        }
    }
}
