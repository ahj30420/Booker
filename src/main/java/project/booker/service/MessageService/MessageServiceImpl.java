package project.booker.service.MessageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.booker.controller.MessageController.dto.*;
import project.booker.domain.Enum.State;
import project.booker.domain.MemberProfile;
import project.booker.domain.Message;
import project.booker.dto.ImgFileDto;
import project.booker.repository.MessageRepository;
import project.booker.repository.PofileRepository.ProfileRepository;
import project.booker.util.ImgStore;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService{

    private final MessageRepository messageRepository;
    private final ProfileRepository profileRepository;
    private final ImgStore imgStore;

    /**
     * 쪽지 보내기
     * 1. senderId, recipientId로 발송자, 수신자 회원 프로필 조회
     * 2. sender,recipient,title,content로 쪽지 작성 및 DB 저장
     */
    @Override
    public void writeMessage(String senderId, WriteMessage writeMessage) {

        String recipientId = writeMessage.getRecipientId();
        String title = writeMessage.getTitle();
        String content = writeMessage.getContent();

        List<MemberProfile> memberProfiles = profileRepository.searchSenderRecipient(senderId, recipientId);

        MemberProfile sender;
        MemberProfile recipient;
        if(memberProfiles.get(0).getProfileId().equals(senderId)){
            sender = memberProfiles.get(0);
            recipient = memberProfiles.get(1);
        } else{
            sender = memberProfiles.get(1);
            recipient = memberProfiles.get(0);
        }

        Message message = Message.createMessage(sender, recipient, title, content);
        messageRepository.save(message);
    }

    /**
     * 보낸 쪽지 보기
     * 1. 사용자의 profileId로 보낸 쪽지 목록 조회
     * 2. DTO로 변환 후 반환
     */
    @Override
    public MessageList viewSentMessage(String senderId) throws IOException {

        MemberProfile sender = profileRepository.findByProfileId(senderId);
        List<Message> sentMessage = messageRepository.findSentMessage(sender);

        MessageList<MessageInfo> messageList = new MessageList<>();
        for (Message message : sentMessage) {
            String messageId = message.getMessageId();
            String title = message.getTitle();
            State state = message.getState();
            LocalDateTime redate = message.getRedate();
            String nickname = message.getRecipient().getNickname();

            String storeImgName = message.getRecipient().getImg().getStoreImgName();
            ImgFileDto imgFileDto = imgStore.getImgFile(storeImgName);

            MessageInfo messageInfo = MessageInfo.builder()
                    .messageId(messageId)
                    .title(title)
                    .state(state)
                    .redate(redate)
                    .nickname(nickname)
                    .imgFileDto(imgFileDto)
                    .build();

            messageList.getMessageList().add(messageInfo);
        }

        return messageList;
    }

    /**
     * 받은 쪽지 보기
     * 1. 사용자의 profileId로 보낸 쪽지 목록 조회
     * 2. DTO로 변환 후 반환
     */
    @Override
    public MessageList viewReceivedMessage(String senderId) throws IOException {

        MemberProfile recipient = profileRepository.findByProfileId(senderId);
        List<Message> receivedMessage = messageRepository.findReceivedMessage(recipient);

        MessageList<MessageInfo> messageList = new MessageList<>();
        for (Message message : receivedMessage) {
            String messageId = message.getMessageId();
            String title = message.getTitle();
            State state = message.getState();
            LocalDateTime redate = message.getRedate();
            String nickname = message.getSender().getNickname();

            String storeImgName = message.getSender().getImg().getStoreImgName();
            ImgFileDto imgFileDto = imgStore.getImgFile(storeImgName);

            MessageInfo messageInfo = MessageInfo.builder()
                    .messageId(messageId)
                    .title(title)
                    .state(state)
                    .redate(redate)
                    .nickname(nickname)
                    .imgFileDto(imgFileDto)
                    .build();

            messageList.getMessageList().add(messageInfo);
        }

        return messageList;
    }

    /**
     * 받은 쪽지 내용 보기
     * 1. 쪽지Id로 쪽지 정보와 보낸 유저 정보 조회
     * 2. Dto로 변환 및 반환
     */
    @Override
    public ReceivedMessageContent viewReceivedMessageContent(String messageId) throws IOException {
        Message message = messageRepository.findFetchSenderByMessageId(messageId);

        String senderId = message.getSender().getProfileId();
        String title = message.getTitle();
        String content = message.getContent();
        String nickname = message.getSender().getNickname();

        String storeImgName = message.getSender().getImg().getStoreImgName();
        ImgFileDto imgFileDto = imgStore.getImgFile(storeImgName);

        ReceivedMessageContent receivedMessageContent = ReceivedMessageContent.builder()
                .senderId(senderId)
                .title(title)
                .content(content)
                .nickname(nickname)
                .imgFileDto(imgFileDto)
                .build();

        return receivedMessageContent;
    }

    /**
     * 보낸 쪽지 내용 보기
     * 1. 쪽지Id로 쪽지 정보와 받은 유저 정보 조회
     * 2. Dto로 변환 및 반환
     */
    @Override
    public SentMessageContent viewSentMessageContent(String messageId) throws IOException {
        Message message = messageRepository.findFetchRecipientByMessageId(messageId);

        String title = message.getTitle();
        String content = message.getContent();
        String nickname = message.getRecipient().getNickname();

        String storeImgName = message.getRecipient().getImg().getStoreImgName();
        ImgFileDto imgFileDto = imgStore.getImgFile(storeImgName);

        SentMessageContent sentMessageContent = SentMessageContent.builder()
                .title(title)
                .content(content)
                .nickname(nickname)
                .imgFileDto(imgFileDto)
                .build();

        return sentMessageContent;
    }

    /**
     * 쪽지 삭제
     */
    @Override
    public void deleteMessage(String messageId) {
        messageRepository.deleteByMessageId(messageId);
    }
}
