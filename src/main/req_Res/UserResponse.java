package req_Res;

/**
 * <code>UserResponse</code> a response object that holds data relevant to the HTTP handlers creating a user
 */
public class UserResponse {
    /**
     * <code>username</code> the username of the user created
     */
    private String username;
    /**
     * <code>authToken</code> the authToken created for this user (updated when each new session is created/deleted)
     */
    private String authToken;
    /**
     * <code>message</code> the error message that is returned if response is a failure response
     */
    private String message;

    public UserResponse(String username, String authToken) {
        this.username = username;
        this.authToken = authToken;
    }

    public UserResponse(String message) {
        this.message = message;
    }

    public UserResponse() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
