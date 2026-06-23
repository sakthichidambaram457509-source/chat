package com.chat.dto;

public class ChatMessage {

    private Long senderId;
    private Long receiverId;
    private String content;
	public Long getSenderId() {
		return senderId;
	}
	public Long getReceiverId() {
		return receiverId;
	}
	public String getContent() {
		return content;
	}
	public void setSenderId(Long senderId) {
		this.senderId = senderId;
	}
	public void setReceiverId(Long receiverId) {
		this.receiverId = receiverId;
	}
	public void setContent(String content) {
		this.content = content;
	}

    // getters setters
}