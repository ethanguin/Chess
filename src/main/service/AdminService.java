package service;

import req_Res.*;

/**
 * <code>AdminService</code> service for admin requests
 */
public class AdminService {
    /**
     * <code>clear</code> clears the database application of all data. Includes users, games, and sessions. This is for testing purposes.
     * Takes no parameters (although in a finished program, would require a high-level authorization request)
     *
     * @return ClearResponse - returns an empty response if it is successful, includes an error message if it is unable to clear the database
     */
    public ClearResponse clear() {
        return new ClearResponse();
    }
}
