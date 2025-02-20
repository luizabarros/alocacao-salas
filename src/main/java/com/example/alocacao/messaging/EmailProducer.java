package com.example.alocacao.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailProducer {
	@Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendEmail(String email) {
        rabbitTemplate.convertAndSend("emailExchange", "emailRoutingKey", email);
    }
}