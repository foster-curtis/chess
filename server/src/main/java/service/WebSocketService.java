package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.*;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.UserGameCommand;

public class WebSocketService extends Service {

    public WebSocketService(GameDAO gameDAO, UserDAO userDAO, AuthDAO authDAO) {
        super(gameDAO, userDAO, authDAO);
    }

    public GameAuthPackage connect(UserGameCommand cmd) throws DataAccessException {
        AuthData authData = authenticate(new AuthData(cmd.getAuthToken(), null));
        GameData gameData = gameAccess.getGame(cmd.getGameID());
        return new GameAuthPackage(gameData, authData);
    }
}
