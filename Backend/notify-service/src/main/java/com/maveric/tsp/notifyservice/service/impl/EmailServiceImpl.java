package com.maveric.tsp.notifyservice.service.impl;

import com.maveric.tsp.notifyservice.dto.EmailRequest;
import com.maveric.tsp.notifyservice.graphApiConfig.GraphApiConnection;
import com.microsoft.graph.models.*;
import com.microsoft.graph.requests.GraphServiceClient;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.LinkedList;

@Service
@Slf4j
public class EmailServiceImpl {

    @Autowired
    GraphApiConnection graphApiConnection;

    private GraphServiceClient<Request> appClient;

    @Value("${spring.mail.username}")
    private String sender;


    public void sendMail(EmailRequest emailRequest) {
        try {
            appClient = graphApiConnection.intializeGraphForAppOnlyAuth();
        } catch (Exception e) {
            log.error("Error Initializing Graph for user auth");
            log.error(e.getMessage());
        }

        try {
            sendMailGraphApi(emailRequest);
            log.info("Mail Sent Successfully");
        } catch (Exception e) {
            log.error("Error while sending maiil");
            log.error(e.getMessage());
        }
    }


    private void sendMailGraphApi(EmailRequest emailRequest) throws Exception {
        log.info("Preparing Email");

        //Ensure client is not null
        if (appClient == null)
            throw new Exception("Graph has not been initialized for user auth");
        Message message = new Message();
        message.subject = emailRequest.getSubject();
        ItemBody body = new ItemBody();
        body.contentType = BodyType.HTML;
        body.content = emailRequest.getMessage();
        message.body = body;
        LinkedList<Recipient> recipients = new LinkedList<Recipient>();
        Recipient recipient = new Recipient();
        EmailAddress emailAddress = new EmailAddress();
        emailAddress.address = emailRequest.getRecipient();
        recipient.emailAddress = emailAddress;
        recipients.add(recipient);
        message.toRecipients = recipients;

        log.info("Sending email");
        appClient.users(sender)
                .sendMail(UserSendMailParameterSet.newBuilder()
                        .withMessage(message)
                        .build())
                .buildRequest()
                .post();
    }
}
