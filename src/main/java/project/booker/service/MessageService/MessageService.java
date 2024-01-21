package project.booker.service.MessageService;

import project.booker.controller.MessageController.dto.ReceivedMessageContent;
import project.booker.controller.MessageController.dto.MessageList;
import project.booker.controller.MessageController.dto.SentMessageContent;
import project.booker.controller.MessageController.dto.WriteMessage;

import java.io.IOException;

public interface MessageService {

    void writeMessage(String senderId, WriteMessage writeMessage);

    MessageList viewSentMessage(String senderId) throws IOException;

    MessageList viewReceivedMessage(String senderId) throws IOException;

    ReceivedMessageContent viewReceivedMessageContent(String messageId) throws IOException;

    SentMessageContent viewSentMessageContent(String messageId) throws IOException;

    void deleteMessage(String messageId);
}
