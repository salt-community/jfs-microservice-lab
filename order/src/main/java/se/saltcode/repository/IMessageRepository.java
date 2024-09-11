package se.saltcode.repository;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import se.saltcode.model.message.Message;

import java.util.UUID;

@Repository
public interface IMessageRepository extends ListCrudRepository<Message, UUID> {
}
