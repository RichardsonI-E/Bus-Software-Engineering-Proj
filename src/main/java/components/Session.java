package components;


import primary.User;

public class Session{
    public static User currentUser = null;

    public static void setUser(User user){
        currentUser = user;
    }

    public static User getUser(){
        return currentUser;
    }

    public static void clearUser(){
        currentUser = null;
    }

}