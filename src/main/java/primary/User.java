package primary;

import java.util.List;
/*this class declares the general user object, which the managers and admin
are extensions of. It holds general information about users to */
public class User {

    /* Attributes for the User superclass */
    private String name; // User's full name
    private String username; // User's unique username for login
    private String password; // User's password for authentication
    //permission of current user; basic, bus/station manager or admin
    private String perms;

    //default constructor, set user attributes to given strings
    public User() {
        name = "John Smith";
        username = "SmithJ";
        password = "123abc";
        perms = "basic";
    }

    //constructor that declares each attribute by user input
    public User(String name, String username, String password, String perms) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.perms = perms;
    }

    /*---------Setters and Getters for User superclass attributes--------- */
    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPerms() {
        return perms;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPerms(String perms) {
        this.perms = perms;
    }

    /* ----------------------------User methods---------------------------- */
    public static boolean login(List<User> users, String username, String password) {
        for (User profile : users) {
            if (profile.getUsername().equals(username) &&
                    profile.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }
}