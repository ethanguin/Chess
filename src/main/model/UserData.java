package model;

import java.util.Objects;

/**
 * <code>UserData</code> class is the object that stores any data related to a specific login session
 */
public class UserData {
    /**
     * the username of the specific user defined by the <code>UserData</code> object
     */
    private final String username;
    /**
     * the password of the user
     */
    private String password;
    /**
     * the email of the user
     */
    private String email;

    public UserData(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public UserData(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserData userData = (UserData) o;
        return Objects.equals(username, userData.username) && Objects.equals(password, userData.password) && Objects.equals(email, userData.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, email);
    }
}
