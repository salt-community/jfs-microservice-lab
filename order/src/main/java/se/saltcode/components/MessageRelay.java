package se.saltcode.components;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import se.saltcode.model.message.Message;
import se.saltcode.repository.IMessageRepository;

import java.util.Objects;

@Component
public class MessageRelay {

    private final IMessageRepository messageRepository;
    private final WebClient webClient;

    public MessageRelay(IMessageRepository messageRepository) {
        this.messageRepository = messageRepository;
        this.webClient = WebClient.create("http://localhost:5000/api/inventory/");
    }


    public void sendUnfinishedMessages() {
        messageRepository.findAll().forEach(this::sendMessage);

    }

    private void sendMessage(Message message) {

        HttpStatusCode response = Objects.requireNonNull(
                webClient.put()
                        .uri(message.getInventoryId()+"/"+message.getQuantityChange())
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .toBodilessEntity()
                        .block()).getStatusCode();

        if(response.is2xxSuccessful()){
            messageRepository.deleteById(message.getId());
        }
    }
}
