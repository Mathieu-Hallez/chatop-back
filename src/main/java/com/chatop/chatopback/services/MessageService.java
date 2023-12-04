package com.chatop.chatopback.services;

import com.chatop.chatopback.model.Message;
import com.chatop.chatopback.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    public Message createMessage(Message message) {
        return this.messageRepository.save(message);
    }
}
