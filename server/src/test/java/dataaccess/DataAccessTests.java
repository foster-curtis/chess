package dataaccess;

import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

public class DataAccessTests {
    private DBUserDAO userAccess;
    private UserData user;

    public DataAccessTests() {
        try {
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
}
