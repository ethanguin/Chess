package req_Res;

import java.util.Objects;

/**
 * <code>SessionResponse</code> a response object that holds data relevant to the HTTP handlers for creating or deleting a session
 */
public class SessionResponse {
    /**
     * the username of the user who logged in
     */
    private String username;
    /**
     * <code>message</code> the error message that is returned if response is a failure response
     */
    private String message;
    /**
     * <code>authToken</code> the authToken of the session created
     */
    private String authToken;

    public SessionResponse() {
    }

    public SessionResponse(String username, String authToken) {
        this.username = username;
        this.authToken = authToken;
    }

    public SessionResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getAuthToken() {
        return authToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SessionResponse that = (SessionResponse) o;
        if (message != null && that.message != null) {
            if (!message.equals(that.message)) {
                return false;
            }
        } else if (!(message == null && that.message == null)) {
            return false;
        }
        if (username != null && that.username != null) {
            if (!username.equals(that.username)) {
                return false;
            }
        } else if (!(username == null && that.username == null)) {
            return false;
        }
        if (authToken != null && that.authToken != null) {
            return authToken.equals(that.authToken);
        } else return authToken == null && that.authToken == null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, authToken);
    }
}
