package com.example.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.entity.Message;
import com.example.exception.InvalidMessageException;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private AccountRepository accountRepository;

    public Message createMessage(Message message) {
        if (message.getMessageText() == null || message.getMessageText().trim().isEmpty()) {
            throw new InvalidMessageException("Message text cannot be blank");
        }

        if (message.getMessageText().length() > 255) {
            throw new InvalidMessageException("Message text cannot exceed 255 characters");
        }

        if (message.getPostedBy() == null || !accountRepository.existsById(message.getPostedBy())) {
            throw new InvalidMessageException("PostedBy must refer to an existing user");
        }

        message.setTimePostedEpoch(System.currentTimeMillis());
        
        return messageRepository.save(message);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Message getMessageById(Integer id) {
        return messageRepository.findById(id).orElse(null);
    }

    public int deleteMessage(Integer id) {
        if (messageRepository.existsById(id)) {
            messageRepository.deleteById(id);
            return 1;
        }
        return 0;
    }

    public int updateMessage(Integer id, String newText) {
        Message currentMessage = getMessageById(id);

        if (currentMessage == null) {
            throw new InvalidMessageException("Message with id: " + id + " not found");
        }

        if (newText == null || newText.trim().isEmpty()) {
            throw new InvalidMessageException("Updated message text cannot be blank");
        }
        if (newText.length() > 255) {
            throw new InvalidMessageException("Updated message text cannot exceed 255 characters");
        }

        currentMessage.setMessageText(newText);
        messageRepository.save(currentMessage);

        return 1;
    }

    public List<Message> getMessagesByAccountId(Integer accountId) {
        return messageRepository.findByPostedBy(accountId);
    }
}
