package passoffTests.serviceTests;

import dataAccess.DataAccess;
import dataAccess.DataAccessException;
import dataAccess.MemoryDataAccess;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import req_Res.UserResponse;
import service.UserService;

public class UserServiceTests {
    UserData testUser = new UserData("ethan", "password123", "me@me.org");
    UserData failUser = new UserData("", "", "");

    @Test
    public void successfulCreateUser() throws DataAccessException {
        UserService.createUser(testUser);
        DataAccess dao = new MemoryDataAccess();
        Assertions.assertEquals(testUser, dao.findUser(testUser));
    }

    @Test
    public void createUserNoData() {
        UserResponse response = UserService.createUser(failUser);
        UserResponse expectedResponse = new UserResponse("Error: bad request");
        DataAccess dao = new MemoryDataAccess();
        Assertions.assertEquals(expectedResponse.getMessage(), response.getMessage());
    }
}
