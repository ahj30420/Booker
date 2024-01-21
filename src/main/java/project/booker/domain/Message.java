package project.booker.domain;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.booker.domain.Enum.State;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messagePk;

    @Column(unique = true)
    private String messageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender")
    private MemberProfile sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient")
    private MemberProfile recipient;

    private String title;
    private String content;

    @Enumerated(EnumType.STRING)
    private State state;

    private LocalDateTime redate;

    //----------------------------------------생성 메서드-------------------------------------------------------
    public static Message createMessage(MemberProfile sender, MemberProfile recipient, String title, String content){
       Message message = new Message();
       message.messageId = NanoIdUtils.randomNanoId();
       message.sender = sender;
       message.recipient = recipient;
       message.title = title;
       message.content = content;
       message.state = State.BEFORE;
       message.redate = LocalDateTime.now();

       return message;
    }
}
