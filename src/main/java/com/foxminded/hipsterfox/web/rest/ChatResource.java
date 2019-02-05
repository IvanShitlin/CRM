package com.foxminded.hipsterfox.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.foxminded.hipsterfox.service.ChatService;
import com.foxminded.hipsterfox.service.dto.EmailMessageDTO;
import com.foxminded.hipsterfox.service.errors.EmailException;
import com.foxminded.hipsterfox.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

@RestController
@RequestMapping("/api")
public class ChatResource {

    private final Logger log = LoggerFactory.getLogger(ChatResource.class);

    private static final String ENTITY_NAME = "chat";

    private final ChatService chatService;

    public ChatResource(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/chat")
    @Timed
    public ResponseEntity<EmailMessageDTO> sendMessage(@RequestBody EmailMessageDTO emailMessageDTO) {
        try {
            chatService.sendMessage(emailMessageDTO);
        } catch (EmailException e) {
            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "chatexception");
        }
        return ResponseEntity.ok(emailMessageDTO);
    }
}
