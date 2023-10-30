package passoffTests.serviceTests;

import dataAccess.DataAccess;
import dataAccess.DataAccessException;
import dataAccess.MemoryDataAccess;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.AdminService;

public class AdminServiceTests {
    UserData testUser = new UserData("ethan", "password123", "me@me.org");

    @Test
    public void successfulClear() throws DataAccessException {
        //add in data into the dao and use clear to clear it
        DataAccess dao = new MemoryDataAccess();
        dao.clear();
        dao.createUser(testUser);
        Assertions.assertEquals(dao.findUser(testUser), testUser);

        AdminService.clear();
        String userMessage;
        try {
            UserData user = dao.findUser(testUser);
            userMessage = "user found!";
        } catch (DataAccessException e) {
            userMessage = "user not found!";
        }
        Assertions.assertEquals("user not found!", userMessage);
    }
}
