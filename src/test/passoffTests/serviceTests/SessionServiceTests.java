package passoffTests.serviceTests;

import dataAccess.DataAccess;
import dataAccess.DataAccessException;
import dataAccess.MemoryDataAccess;
import dataAccess.SQLDataAccess;
import model.SessionData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import req_Res.SessionResponse;
import service.SessionService;

public class SessionServiceTests {
    UserData testUser = new UserData("ethan", "password123", "me@me.org");
    UserData failUser = new UserData("john", "", "me@me.org");
    String authToken = "12345";

    @Test
    public void successfulCreateSession() throws DataAccessException {
        DataAccess dao = new SQLDataAccess();
        dao.clear();
        dao.createUser(testUser);

        SessionResponse response = SessionService.createSession(testUser);
        SessionData session = new SessionData(testUser.getUsername());
        session.setAuthToken(response.getAuthToken());

        Assertions.assertNull(response.getMessage());
    }

    @Test
    public void wrongPasswordCreateSession() throws DataAccessException {
        DataAccess dao = new SQLDataAccess();
        dao.clear();
        dao.createUser(testUser);

        SessionResponse response = SessionService.createSession(new UserData(testUser.getUsername(), failUser.getPassword(), null));

        Assertions.assertEquals("Error: unauthorized", response.getMessage());
    }

    @Test
    public void successfulDeleteSession() throws DataAccessException {
        DataAccess dao = new SQLDataAccess();
        dao.clear();
        dao.createUser(testUser);
        SessionData session = new SessionData(authToken, testUser.getUsername());
        dao.createSession(session);

        SessionResponse response = SessionService.deleteSession(session);

        Assertions.assertNull(response.getMessage());
    }

    @Test
    public void wrongAuthDeleteSession() throws DataAccessException {
        DataAccess dao = new SQLDataAccess();
        dao.clear();
        dao.createUser(testUser);
        SessionData session = new SessionData(authToken, testUser.getUsername());
        dao.createSession(session);
        SessionData wrongSession = new SessionData("fish123", testUser.getUsername());

        SessionResponse response = SessionService.deleteSession(wrongSession);

        Assertions.assertEquals("Error: unauthorized", response.getMessage());
    }
}
