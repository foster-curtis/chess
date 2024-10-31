package dataaccess;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

public class DataAccessTests {

    @Test
    public void testDBandTableCreation() {
        try {
            new DBUserDAO();
        } catch (DataAccessException e) {
            fail(e.getMessage());
        }

    }
}
