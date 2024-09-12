package se.saltcode.configuration;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import se.saltcode.model.message.Message;
import se.saltcode.repository.IMessageRepository;

import java.util.Objects;

@Component
public class AppSetup implements InitializingBean {

    private final IMessageRepository messageRepository;
    private final WebClient webClient;

    public AppSetup(IMessageRepository messageRepository) {
        this.messageRepository = messageRepository;
        this.webClient = WebClient.create();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        sendUnfinishedMessages();
    }

    private void sendUnfinishedMessages() {
        messageRepository.findAll().forEach(this::sendMessage);

    }

    private void sendMessage(Message message) {
        HttpStatusCode response = Objects.requireNonNull(
                webClient.method(HttpMethod.valueOf(message.getMessageType().name()))
                .uri(message.getUri())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toBodilessEntity()
                .block()).getStatusCode();

        if(response.is2xxSuccessful()){
            messageRepository.deleteById(message.getId());
        }
    }
}