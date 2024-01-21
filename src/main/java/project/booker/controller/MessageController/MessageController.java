package project.booker.controller.MessageController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import project.booker.controller.MessageController.dto.ReceivedMessageContent;
import project.booker.controller.MessageController.dto.MessageList;
import project.booker.controller.MessageController.dto.SentMessageContent;
import project.booker.controller.MessageController.dto.WriteMessage;
import project.booker.dto.AuthenticatedUser;
import project.booker.exception.exceptions.ValidationException;
import project.booker.service.MessageService.MessageService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final MessageSource messageSource;

    /**
     * 쪽지 보내기
     * 1. 쪽지 제목, 내용 검증
     * 2. 쪽지 내용 DB에 저장
     */
    @PostMapping("/message")
    public void writeMessage(HttpServletRequest request,
                             @Validated @RequestBody WriteMessage writeMessage,
                             BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            sendValidationError(bindingResult);
        }

        AuthenticatedUser authenticatedUser = (AuthenticatedUser) request.getAttribute("AuthenticatedUser");
        String senderId = authenticatedUser.getProfileId();

        messageService.writeMessage(senderId, writeMessage);
    }

    /**
     * 보낸 쪽지 보기
     */
    @GetMapping("/sent/message")
    public MessageList viewSentMessage(HttpServletRequest request) throws IOException {

        AuthenticatedUser authenticatedUser = (AuthenticatedUser) request.getAttribute("AuthenticatedUser");
        String senderId = authenticatedUser.getProfileId();

        return messageService.viewSentMessage(senderId);
    }

    /**
     * 받은 쪽지 보기
     */
    @GetMapping("/received/message")
    public MessageList viewReceivedMessage(HttpServletRequest request) throws IOException {

        AuthenticatedUser authenticatedUser = (AuthenticatedUser) request.getAttribute("AuthenticatedUser");
        String senderId = authenticatedUser.getProfileId();

        return messageService.viewReceivedMessage(senderId);
    }

    /**
     * 받은 쪽지 내용 보기
     */
    @GetMapping("/received/message/content")
    public ReceivedMessageContent viewReceivedMessageContent(@RequestParam("messageId") String messageId) throws IOException {
        return messageService.viewReceivedMessageContent(messageId);
    }

    /**
     * 보낸 쪽지 내용 보기
     */
    @GetMapping("/sent/message/content")
    public SentMessageContent viewSentMessageContent(@RequestParam("messageId") String messageId) throws IOException {
        return messageService.viewSentMessageContent(messageId);
    }

    /**
     * 쪽지 삭제
     */
    @DeleteMapping("/message")
    public void deleteMessage(@RequestParam("messageId") String messageId){
        messageService.deleteMessage(messageId);
    }

    //--------------------------------------Private Method-----------------------------------------------------

    /**
     * 검증 실패시 오류 메시지를 담아 Exception을 발생 시켜주는 함수
     */
    private void sendValidationError(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        for(FieldError error : bindingResult.getFieldErrors()){
            String message = messageSource.getMessage(error, Locale.getDefault());
            errors.put(error.getField(),message);
        }
        throw new ValidationException(errors);
    }

}
