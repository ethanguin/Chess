package req_Res;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserResponse that = (UserResponse) o;
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
        return Objects.hash(username, authToken, message);
    }
}
