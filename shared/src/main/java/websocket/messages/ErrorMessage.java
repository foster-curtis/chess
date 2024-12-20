package websocket.messages;

public class ErrorMessage extends ServerMessage {
    private final String errorMessage;

    public ErrorMessage(String error) {
        super(ServerMessageType.ERROR);
        this.errorMessage = "Error: " + error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
