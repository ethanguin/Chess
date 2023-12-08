package ui;

import com.google.gson.Gson;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {

    Session session;
    DisplayRequests responseHandler;

    public WebSocketFacade(String url, DisplayRequests responseHandler) throws DeploymentException, IOException, URISyntaxException {
        URI socketURI = new URI(url);

        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, socketURI);

        this.session.addMessageHandler((MessageHandler.Whole<String>) message -> {
            var gson = new Gson();

            try {
                ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                switch (serverMessage.getServerMessageType()) {
                    case ERROR -> responseHandler.error(gson.fromJson(message, ErrorMessage.class).errorMessage);

                    case NOTIFICATION ->
                            responseHandler.message(gson.fromJson(message, NotificationMessage.class).message);
                    case LOAD_GAME -> responseHandler.updateBoard(gson.fromJson(message, LoadMessage.class).game);
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        });
    }

    public void sendCommand(UserGameCommand command) throws IOException {
        this.session.getBasicRemote().sendText(new Gson().toJson(command));
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}
