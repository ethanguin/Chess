package service;

import model.UserData;
import req_Res.UserResponse;

/**
 * <code>UserService</code> service for requests relating to user data
 */
public class UserService {
    /**
     * <code>createUser</code> creates a new user in the database.
     *
     * @param user - includes username, password, and email of the new user
     * @return UserResponse - returns the username and authToken of the new user if successful. If unsuccessful, it returns an error message
     */
    public UserResponse createUser(UserData user) {
        return new UserResponse();
    }
}
