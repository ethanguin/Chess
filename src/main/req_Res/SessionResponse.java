package req_Res;

/**
 * <code>SessionResponse</code> a response object that holds data relevant to the HTTP handlers for creating or deleting a session
 */
public class SessionResponse {
    /**
     * <code>message</code> the error message that is returned if response is a failure response
     */
    private String message;
    /**
     * <code>authToken</code> the authToken of the session created
     */
    private String authToken;

    public SessionResponse(String message, String authToken) {
        this.message = message;
        this.authToken = authToken;
    }

    public SessionResponse() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
