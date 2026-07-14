package com.chat.controller;

import com.chat.model.Message;
import com.chat.repository.MessageRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/messages")
@CrossOrigin(origins="*")
public class MessageController {

    private final MessageRepository messageRepository;

    public MessageController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @PostMapping
    public Message sendMessage(@RequestBody Message message) {
        message.setTimestamp(LocalDateTime.now());
        return messageRepository.save(message);
    }

    @GetMapping
    public List<Message> getMessages() {
        return messageRepository.findAll();
    }

    @GetMapping("/chat")
    public List<Message> getChatHistory(
            @RequestParam Long senderId,
            @RequestParam Long receiverId) {

        return messageRepository.getChatHistory(senderId, receiverId);
    }
}