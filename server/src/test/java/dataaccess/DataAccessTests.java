package dataaccess;

import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.fail;

public class DataAccessTests {
    private DBUserDAO userAccess;
    private DBAuthDAO authAccess;
    private UserData user;

    public DataAccessTests() {
        try {
            this.authAccess = new DBAuthDAO();
            this.userAccess = new DBUserDAO();
            this.user = new UserData("bob", "12345", "email@email.com");
        } catch (DataAccessException e) {
            System.out.println("unable to initialize database connection: " + e.getMessage());
        }
    }

    @BeforeEach
    public void setup() {
        try {
            userAccess.clear();
            authAccess.clear();
        } catch (DataAccessException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testDBandTableCreation() {
        try {
            new DBUserDAO();

        } catch (DataAccessException e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void testCreateUserSuccess() {
        Assertions.assertDoesNotThrow(() -> userAccess.createUser(user));
    }

    @Test
    public void testCreateUserFail() {
        UserData badUser = new UserData(null, null, null);
        Assertions.assertThrows(DataAccessException.class, () -> userAccess.createUser(badUser));
    }

    @Test
    public void testGetUserSuccess() {
        Assertions.assertDoesNotThrow(() -> userAccess.createUser(user));
        try {
            Assertions.assertEquals(user, userAccess.getUser(user));
        } catch (DataAccessException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetUserFail() {
        UserData badUser = new UserData("jimmy", null, null);
        Assertions.assertDoesNotThrow(() -> userAccess.createUser(user));
        try {
            Assertions.assertNull(userAccess.getUser(badUser));
        } catch (DataAccessException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testCreateAuth() {
        UUID authToken = UUID.randomUUID();
        Assertions.assertDoesNotThrow(() -> authAccess.createAuth(user, String.valueOf(authToken)));
    }

    @Test
    public void testCreateAuthFail() {
        UUID authToken = UUID.randomUUID();
        UserData badUser = new UserData(null, null, null);
        Assertions.assertThrows(DataAccessException.class, () -> authAccess.createAuth(badUser, String.valueOf(authToken)));
        Assertions.assertThrows(DataAccessException.class, () -> authAccess.createAuth(user, null));
    }

    @Test
    public void testGetAuth() {
        UUID authToken = UUID.randomUUID();
        Assertions.assertDoesNotThrow(() -> authAccess.createAuth(user, String.valueOf(authToken)));
        try {
            Assertions.assertEquals(new AuthData(String.valueOf(authToken), user.username()), authAccess.getAuth(new AuthData(String.valueOf(authToken), null)));
        } catch (DataAccessException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetAuthFail() {
        UUID authToken = UUID.randomUUID();
        Assertions.assertDoesNotThrow(() -> authAccess.createAuth(user, String.valueOf(authToken)));
        try {
            Assertions.assertNull(authAccess.getAuth(new AuthData("12345", null)));
        } catch (DataAccessException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testDeleteAuth() {
        UUID authToken = UUID.randomUUID();
        Assertions.assertDoesNotThrow(() -> authAccess.createAuth(user, String.valueOf(authToken)));
        Assertions.assertDoesNotThrow(() -> authAccess.deleteAuth(new AuthData(String.valueOf(authToken), user.username())));
        try {
            Assertions.assertNull(authAccess.getAuth(new AuthData(String.valueOf(authToken), user.username())));
        } catch (DataAccessException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testDeleteAuthFail() {
        UUID authToken = UUID.randomUUID();
        Assertions.assertDoesNotThrow(() -> authAccess.createAuth(user, String.valueOf(authToken)));
        try {
            authAccess.deleteAuth(new AuthData(String.valueOf(1234565), user.username()));
            Assertions.assertEquals(new AuthData(String.valueOf(authToken), user.username()), authAccess.getAuth(new AuthData(String.valueOf(authToken), user.username())));
        } catch (DataAccessException e) {
            fail(e.getMessage());
        }
    }
}
