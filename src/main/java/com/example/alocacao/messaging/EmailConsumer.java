package com.example.alocacao.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import java.io.IOException;

@Component
public class EmailConsumer {    

    private final String sendGridApiKey;
    private final String sendGridEmail;

    public EmailConsumer(@Value("${sendgrid.api.key}") String sendGridApiKey, @Value("${sendgrid.sender.email}") String sendGridEmail) {
        this.sendGridApiKey = sendGridApiKey;
        this.sendGridEmail = sendGridEmail;
    }

    @RabbitListener(queues = "emailQueue")
    public void receiveEmail(String email) {  
        try {
            sendEmail(email);
        } catch (IOException e) {
            System.err.println("Erro ao enviar e-mail para " + email + ": " + e.getMessage());
        }
    }

    private void sendEmail(String to) throws IOException {    			
        Email from = new Email(sendGridEmail);
        Email recipient = new Email(to);
        Content content = new Content("text/plain", "Olá, seja bem-vindo! Sua conta foi criada com sucesso.");
        Mail mail = new Mail(from, "Bem-vindo ao sistema de alocação de salas!", recipient, content);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());

        Response response = sg.api(request);
        System.out.println("E-mail enviado para " + to + " com status: " + response.getStatusCode());
    }
}
