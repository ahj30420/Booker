package project.booker.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.booker.domain.MemberProfile;
import project.booker.domain.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("select m from Message m where m.sender = :sender order by m.redate desc")
    @EntityGraph(attributePaths = {"recipient"})
    List<Message> findSentMessage(@Param("sender") MemberProfile sender);

    @Query("select m from Message m where m.recipient = :recipient order by m.redate desc")
    @EntityGraph(attributePaths = {"sender"})
    List<Message> findReceivedMessage(@Param("recipient") MemberProfile recipient);

    @EntityGraph(attributePaths = {"sender"})
    Message findFetchSenderByMessageId(String messageId);

    @EntityGraph(attributePaths = {"recipient"})
    Message findFetchRecipientByMessageId(String messageId);

    void deleteByMessageId(String messageId);
}
