package passoffTests.serviceTests;

import dataAccess.DataAccess;
import dataAccess.DataAccessException;
import dataAccess.MemoryDataAccess;
import model.SessionData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import req_Res.SessionResponse;
import service.SessionService;

public class SessionServiceTests {
    UserData testUser = new UserData("ethan", "password123", "me@me.org");
    String authToken = "12345";

    @Test
    public void successfulCreateSession() throws DataAccessException {
        DataAccess dao = new MemoryDataAccess();
        dao.clear();
        dao.createUser(testUser);

        SessionResponse response = SessionService.createSession(testUser);
        SessionData session = new SessionData(testUser.getUsername());
        session.setAuthToken(response.getAuthToken());

        Assertions.assertNotNull(dao.findSession(session));
    }

    @Test
    public void wrongPasswordCreateSession() throws DataAccessException {
        DataAccess dao = new MemoryDataAccess();
        dao.clear();
        dao.createUser(testUser);
        SessionData session = new SessionData(authToken, testUser.getUsername());
        dao.createSession(session);

        SessionResponse response = SessionService.deleteSession(session);

        Assertions.assertNull(dao.findSession(session));
    }
}
