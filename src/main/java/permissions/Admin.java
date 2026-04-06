package permissions;

import java.util.List;

import json.JsonUtilities;
import primary.User;

public class Admin extends User {

    private static final Admin instance = new Admin();
    // Created Arraylist for users
    private static List<User> users = JsonUtilities.loadUsers();

    private Admin() {
    }// default constructor

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

    public static void addUser(User user) {
        users.add(user);
        JsonUtilities.saveUsers(users);
    }

    public static void deleteUser(User user){
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(user.getUsername())) {
                users.remove(i);
                break;
            }
        }
        JsonUtilities.saveUsers(users);
    }

    public static void updateUser(User updatedUser) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(updatedUser.getUsername())) {
                users.set(i, updatedUser);
                break;
            }
        }

        JsonUtilities.saveUsers(users);
    }

    public static void updateUser(User updatedUser, String ogUsername) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(ogUsername)) {
                users.set(i, updatedUser);
                break;
            }
        }

        JsonUtilities.saveUsers(users);
    }
}
