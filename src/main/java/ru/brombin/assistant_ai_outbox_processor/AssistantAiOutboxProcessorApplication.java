package ru.brombin.assistant_ai_outbox_processor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableRetry
@EnableScheduling
@SpringBootApplication
public class AssistantAiOutboxProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(AssistantAiOutboxProcessorApplication.class, args);
	}

}
