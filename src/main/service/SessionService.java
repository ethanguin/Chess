package service;

import dataAccess.DataAccess;
import dataAccess.DataAccessException;
import dataAccess.SQLDataAccess;
import model.SessionData;
import model.UserData;
import req_Res.SessionResponse;

/**
 * <code>SessionService</code> service for requests relating to the login sessions
 */
public class SessionService {
    /**
     * <code>createSession</code> creates a new login session of an existing user.
     *
     * @param user - the UserRequest, which includes the username and password of the user
     * @return SessionResponse - returns an authToken and username of the user, or an error when unable to complete the request
     */
    public static SessionResponse createSession(UserData user) {
        String unauthorizedError = "Error: unauthorized";
        if (user.getUsername() == null || user.getPassword() == null || user.getUsername().isEmpty() || user.getPassword().isEmpty()) {
            return new SessionResponse(unauthorizedError);
        }
        DataAccess dao;
        try {
            dao = new SQLDataAccess();
        } catch (DataAccessException e) {
            return new SessionResponse(e.getMessage());
        }
        UserData foundUser;
        try {
            foundUser = dao.findUser(user);
            if (foundUser == null) {
                return new SessionResponse(unauthorizedError);
            }
        } catch (DataAccessException e) {
            return new SessionResponse(e.getMessage());
        }
        if (!foundUser.getPassword().equals(user.getPassword())) {
            return new SessionResponse(unauthorizedError);
        }

        try {
            SessionData session = new SessionData(user.getUsername());
            dao.createSession(session);
            return new SessionResponse(user.getUsername(), session.getAuthToken());
        } catch (DataAccessException e) {
            return new SessionResponse(e.getMessage());
        }
    }

    /**
     * <code>deleteSession</code> deletes a login session of the user, logging them out and de-listing the authToken they were using.
     *
     * @param session - includes the session authToken
     * @return SessionResponse - returns an empty response if it is able to complete the request. It includes the error message if unsuccessful
     */
    public static SessionResponse deleteSession(SessionData session) {
        if (session.getAuthToken() == null || session.getAuthToken().isEmpty()) {
            return new SessionResponse("Error: unauthorized");
        }
        try {
            DataAccess dao = new SQLDataAccess();
            dao.deleteSession(session);
            return new SessionResponse();
        } catch (DataAccessException e) {
            return new SessionResponse(e.getMessage());
        }


    }
}
