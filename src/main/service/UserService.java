package service;

import dataAccess.DataAccess;
import dataAccess.DataAccessException;
import dataAccess.SQLDataAccess;
import model.SessionData;
import model.UserData;
import req_Res.UserResponse;

/**
 * <code>UserService</code> service for requests relating to user data
 */
public class UserService {
    /**
     * creates a new user in the database.
     *
     * @param user - includes username, password, and email of the new user
     * @return UserResponse - returns the username and authToken of the new user if successful. If unsuccessful, it returns an error message
     */
    public static UserResponse createUser(UserData user) {
        if (user.getUsername() == null || user.getPassword() == null || user.getEmail() == null || user.getUsername().isEmpty() || user.getPassword().isEmpty() || user.getEmail().isEmpty()) {
            return new UserResponse("Error: bad request");
        }
        try {
            DataAccess dao = new SQLDataAccess();
            if (dao.findUser(user) != null) {
                return new UserResponse("Error: already taken");
            }
            dao.createUser(user);
            SessionData session = new SessionData(user.getUsername());
            dao.createSession(session);
            return new UserResponse(user.getUsername(), session.getAuthToken());
        } catch (DataAccessException e) {
            return new UserResponse(e.getMessage());
        }
    }
}
