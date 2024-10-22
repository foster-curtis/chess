package serviceTests;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import service.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

public class TestService {
    private ChessService service;

    @BeforeEach
    public void setup() {
        var auth = new MemoryAuthDAO();
        var game = new MemoryGameDAO();
        var user = new MemoryUserDAO();
        this.service = new ChessService(game, user, auth);
    }

    @Test
    public void registerUserSuccess() {
        UserData user = new UserData("bob", "12345", "ih@veanemail");
        try {
            var auth = service.register(user);
            Assertions.assertInstanceOf(AuthData.class, auth);
        } catch (DataAccessException exception) {
            fail("DataAccessException");
        }
    }

    @Test
    public void registerUserFail() {
        UserData user = new UserData("bob", "12345", "ih@veanemail");
        try {
            var auth = service.register(user);
            var auth2 = service.register(user);
            fail("User was registered twice");
        } catch (DataAccessException exception) {
            Assertions.assertInstanceOf(DataAccessException.class, exception);
        }
    }
}
