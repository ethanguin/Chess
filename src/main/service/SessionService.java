package service;

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
    public SessionResponse createSession(UserData user) {
        return new SessionResponse();
    }

    /**
     * <code>deleteSession</code> deletes a login session of the user, logging them out and de-listing the authToken they were using.
     *
     * @param session - includes the session authToken
     * @return SessionResponse - returns an empty response if it is able to complete the request. It includes the error message if unsuccessful
     */
    public SessionResponse deleteSession(SessionData session) {
        return new SessionResponse();
    }
}
