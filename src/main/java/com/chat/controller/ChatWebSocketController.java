package com.chat.controller;

import com.chat.dto.ChatMessage;
import com.chat.model.Message;
import com.chat.model.User;
import com.chat.repository.MessageRepository;
import com.chat.repository.UserRepository;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatWebSocketController {
	
	 @Autowired
	    private MessageRepository messageRepository;
	 
	  @Autowired
	    private UserRepository userRepository;
	 

	  @MessageMapping("/send")
	  @SendTo("/topic/messages")
	  public Message sendMessage(ChatMessage chatMessage) {
		  
		  System.out.println("Sender ID = " + chatMessage.getSenderId());
		    System.out.println("Receiver ID = " + chatMessage.getReceiverId());


	      User sender = userRepository
	              .findById(chatMessage.getSenderId())
	              .orElseThrow();

	      User receiver = userRepository
	              .findById(chatMessage.getReceiverId())
	              .orElseThrow();

	      Message message = new Message();

	      message.setSender(sender);
	      message.setReceiver(receiver);
	      message.setContent(chatMessage.getContent());
	      message.setTimestamp(LocalDateTime.now());

	      return messageRepository.save(message);
	  }
}