package ui;

import exception.ResponseException;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {

    Session session;

    public WebSocketFacade(int port) {
        try {
            URI socketURI = new URI("ws://localhost:" + port + "/ws");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            System.out.println("Websocket connection established.");

            //set message handler

        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(ex.getMessage(), 500);
        }

    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}
