package org.labmonkeys.apiclient;

import java.net.URI;
import java.util.UUID;

import javax.inject.Singleton;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.jboss.logging.Logger;
import org.labmonkeys.apiclient.api.ClientApi;
import org.labmonkeys.apiclient.dto.MessageDto;

import io.quarkus.scheduler.Scheduled;

@Singleton
public class ClientApp {

    final Logger LOG = Logger.getLogger(ClientApp.class);

    @ConfigProperty(name = "api-server.url")
    private String url;
    
    @Scheduled(every = "5s")
    public void sendMessage() {

        LOG.info("Scheduler Fired");
        MessageDto message = new MessageDto();
        message.setMessageId(UUID.randomUUID());
        message.setMessage("Hello There");
        LOG.info("Sending message: " + message);
        ClientApi api = RestClientBuilder.newBuilder().baseUri(URI.create(this.url)).build(ClientApi.class);
        try {
            Response response = api.receiveMessage(message);
            LOG.info(response.getStatus());
            MessageDto responseMessage = response.readEntity(MessageDto.class);
            LOG.info(responseMessage);
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
    }
}
