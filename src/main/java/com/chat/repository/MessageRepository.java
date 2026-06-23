package com.chat.repository;

import com.chat.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

	@Query("""
			SELECT m FROM Message m
			WHERE (m.sender.id = :senderId AND m.receiver.id = :receiverId)
			   OR (m.sender.id = :receiverId AND m.receiver.id = :senderId)
			ORDER BY m.timestamp
			""")
			List<Message> getChatHistory(Long senderId, Long receiverId);
	
	List<Message>
	findBySenderIdAndReceiverIdOrSenderIdAndReceiverIdOrderByTimestampAsc(
	    Long senderId,
	    Long receiverId,
	    Long receiverId2,
	    Long senderId2
	);
    
}