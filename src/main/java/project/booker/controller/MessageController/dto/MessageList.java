package project.booker.controller.MessageController.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class MessageList<T> {
    private List<T> messageList = new ArrayList<>();
}
