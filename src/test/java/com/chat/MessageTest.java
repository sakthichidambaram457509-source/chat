package com.chat;

import com.chat.controller.ChatWebSocketController;
import com.chat.dto.ChatMessage;
import com.chat.model.Message;
import com.chat.model.User;
import com.chat.repository.MessageRepository;
import com.chat.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class MessageTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ChatWebSocketController chatWebSocketController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSendWebSocketMessage() {
        User sender = new User();
        sender.setId(1L);
        sender.setUsername("alice");

        User receiver = new User();
        receiver.setId(2L);
        receiver.setUsername("bob");

        when(userRepository.findById(1L)).thenReturn(Optional.of(sender));
        when(userRepository.findById(2L)).thenReturn(Optional.of(receiver));

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSenderId(1L);
        chatMessage.setReceiverId(2L);
        chatMessage.setContent("Hello Bob!");

        Message savedMessage = new Message();
        savedMessage.setSender(sender);
        savedMessage.setReceiver(receiver);
        savedMessage.setContent("Hello Bob!");

        when(messageRepository.save(any(Message.class))).thenReturn(savedMessage);

        Message result = chatWebSocketController.sendMessage(chatMessage);

        assertNotNull(result);
        assertEquals("Hello Bob!", result.getContent());
        assertEquals("alice", result.getSender().getUsername());
        assertEquals("bob", result.getReceiver().getUsername());
    }
}
