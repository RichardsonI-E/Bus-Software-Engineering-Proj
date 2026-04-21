package components;


import primary.User;

/*This class is used as a global indicator to hold the
user that is currently logged in, for getting permissions, name, etc.
*/
public class Session{
    //variable to hold the current user in the session
    public static User currentUser = null;

    //set the session's current user
    public static void setUser(User user){
        currentUser = user;
    }

    //get the session's current user
    public static User getUser(){
        return currentUser;
    }

    //clear the session's current user when logging out
    public static void clearUser(){
        currentUser = null;
    }

}