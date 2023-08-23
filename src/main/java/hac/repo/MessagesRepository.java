package hac.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Messages Repository for querying messages from the database.
 */
public interface MessagesRepository extends JpaRepository<Messages, Long> {
    List<Messages> findByAuthorAndReceiverOrReceiverAndAuthor(User author, User receiver, User author2, User receiver2);
}