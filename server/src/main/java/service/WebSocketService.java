package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.*;
import websocket.commands.UserGameCommand;

import java.util.Objects;

public class WebSocketService extends Service {

    public WebSocketService(GameDAO gameDAO, UserDAO userDAO, AuthDAO authDAO) {
        super(gameDAO, userDAO, authDAO);
    }

    public GameUsernamePackage connect(UserGameCommand cmd) throws DataAccessException {
        AuthData authData = authenticate(new AuthData(cmd.getAuthToken(), null));
        return new GameUsernamePackage(gameAccess.getGame(cmd.getGameID()), authData.username());
    }

    public String leave(UserGameCommand cmd, Boolean gameExpired) throws DataAccessException {
        AuthData authData = authenticate(new AuthData(cmd.getAuthToken(), null));

        if (gameExpired) {
            gameAccess.deleteGame(cmd.getGameID());
        } else {
            GameData game = gameAccess.getGame(cmd.getGameID());

            if (Objects.equals(authData.username(), game.whiteUsername())) {
                var newGame = new GameData(game.gameID(), null, game.blackUsername(), game.gameName(), game.game());
                gameAccess.updateGame(newGame);
            } else if (Objects.equals(authData.username(), game.blackUsername())) {
                var newGame = new GameData(game.gameID(), game.whiteUsername(), null, game.gameName(), game.game());
                gameAccess.updateGame(newGame);
            }
        }
        return authData.username();
    }

    public String resign(UserGameCommand cmd) throws DataAccessException {
        var auth = authenticate(new AuthData(cmd.getAuthToken(), null));
        return auth.username();
    }
}
