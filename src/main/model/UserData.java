package model;

/**
 * <code>UserData</code> class is the object that stores any data related to a specific login session
 */
public class UserData {
    /**
     * the username of the specific user defined by the <code>UserData</code> object
     */
    private String username;
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

    public UserData() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
