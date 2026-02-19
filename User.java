public class User {
    /* Attributes for the User superclass */
    private String name; //User's full name
    private String username; // User's unique username for login
    private String password; // User's password for authentication

    public User(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }

/*----------------------------Setters and Getters for User superclass attributes---------------------------- */
    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
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
/* ----------------------------User Subclasses---------------------------- */

/* Subclass for station manager(s) that extends User */
    public class stationManager extends User {
        private int sManagerID; //Unique identifier for station manager
        public stationManager(String name, String username, String password) {
            super(name, username, password);
        }
        /* Getter and Setter for sManagerID */
        public int getSManagerID() {
            return sManagerID;
        }
        public void setSManagerID(int sManagerID) {
            this.sManagerID = sManagerID;
        }
    }


/* Subclass for bus manager(s) that extends User */
    public class busManager extends User {
        private int bManagerID; //Unique identifier for bus manager
        public busManager(String name, String username, String password) {
            super(name, username, password);
        }
        /* Getter and Setter for bManagerID */
        public int getBManagerID() {
            return bManagerID;
        }
        public void setBManagerID(int bManagerID) {
            this.bManagerID = bManagerID;
        }
    }
}