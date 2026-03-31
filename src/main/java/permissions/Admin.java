package permissions;

import java.util.List;

import json.JsonUtilities;
import primary.User;

public class Admin extends User {
    private static final Admin instance = new Admin();
    // Created Arraylist for users
    private static List<User> users = JsonUtilities.loadUsers();

    private Admin() {
    }; // default constructor

    public Admin(String name, String username, String password, String perms) {
        super(name, username, password, "admin");
    }

    // setters and getters
    public static Admin getInstance() {
        return instance;
    }

    public static List<User> getUsers() {
        return users;
    }

    public static void setUsers(List<User> users) {
        Admin.users = users;
        JsonUtilities.saveUsers(users);
    }
    // main methods

    public static void addUser(User user) {
        users.add(user);
        JsonUtilities.saveUsers(users);
    }


}
