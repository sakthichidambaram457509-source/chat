package com.chat.controller;

import com.chat.model.Message;
import com.chat.repository.MessageRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/all-messages")
@CrossOrigin(origins="*")
public class ChatController {

    private final MessageRepository messageRepository;

    public ChatController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @GetMapping
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }
    
    @GetMapping("/chat")
    public List<Message> getChat(
            @RequestParam Long senderId,
            @RequestParam Long receiverId
    ) {

        return messageRepository.getChatHistory(senderId, receiverId);
    }
}