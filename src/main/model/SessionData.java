package model;

import java.util.Objects;
import java.util.UUID;

/**
 * <code>SessionData</code> class is the object that stores any data related to a specific login session
 */
public class SessionData {
    /**
     * a unique alphanumerical code that allows a user to submit requests while logged in
     */
    private String authToken;

    /**
     * the username of the user who started the session (the user who's logged in)
     */
    private String username;

    public SessionData() {
    }

    public SessionData(String authToken, String username) {
        this.authToken = authToken;
        this.username = username;
    }

    public SessionData(String username) {
        this.username = username;
        this.authToken = generateAuthToken();
    }

    public String getAuthToken() {
        return authToken;
    }

    private String generateAuthToken() {
        authToken = UUID.randomUUID().toString();
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SessionData that = (SessionData) o;
        return Objects.equals(authToken, that.authToken) && Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authToken, username);
    }
}
