package primary;

import java.util.List;

public class User {

    /* Attributes for the User superclass */
    private String name; //User's full name
    private String username; // User's unique username for login
    private String password; // User's password for authentication
    private String perms;

    public User() {
        name = "John Smith";
        username = "SmithJ";
        password = "123abc";
        perms = "basic";
    }

    public User(String name, String username, String password, String perms) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.perms = perms;
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

    public String getPerms(){
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

    public void setPerms(String perms){
        this.perms = perms;
    }

/*  ----------------------------User methods---------------------------- */
    public static boolean login(List<User> users, String username, String password) {
        System.out.print("Input Username: ");
        username = sc.nextLine();

        System.out.println(); //Break for ease of use

        System.out.print("Input Password: ");
        password = sc.nextLine();

        for (User profile : users) {
            if (profile.getUsername().equals(username) &&
                profile.getPassword().equals(password)) {
                return true;
        }
    }
    return false;
}
// LOGIN kinda done. Refine
// public boolean login(){
        //System print: Input username
        //Scanner equals new Scanner: userInput

        //System print: Input password
        //Scanner equals new Scanner: passInput

        // for each profile:
            //if userInput equals Username
                //continue
                //if passInput equals Password:
                    //return true
        //Otherwise, return false (after loop ends)
    //}

    public void logout(){
        System.out.println("You have been logged out and the program will end.");
        System.exit(0);
    }
    // Log out done
    // public void logout(){
        //System print: You have been logged out.
    //}

    public void updateProfile(){
        System.out.println("Change Name? (yes/no)");
        String input = sc.nextLine();
        
        if (input.equalsIgnoreCase("yes")){
            System.out.println("Input your new First and Last name.");
            String nameInput = sc.nextLine();
            setName(nameInput);

            //Split first name from last name
            String[] parts = nameInput.trim().split(" ");
            if (parts.length >= 2){
                String firstName = parts[0];
                String lastName = parts[parts.length - 1];

                //Create new Username
                String newUsername = lastName + firstName.charAt(0);
                setUsername(newUsername);
            }
            else{System.out.println("Invalid Username input. Username not updated.");}

            //Create new Password
            System.out.println("Input your new password.");
            String newPassword = sc.nextLine();
            setPassword(newPassword);
        }
    }

    //Done
    //public void updateProfile(){
        //System print: Change Name?
        //if button equals yes:
            //System print: Input new full name
            //Scanner equals new Scanner:  nameInput
            //setName(nameInput)
        // Java regex: newUserName = ([last word] + [first letter of first word])
        // setUsername(newUserName)
        //System print: Input new password
        //Scanner equals new Scanner: passwordInput
        //setPassword(passwordInput)
    //}

    /*-------------PLEASE READ --------------
    Current errors until Error Route class is resolved and we figure out
        how to have the classes communicate properly.*/
    public travelRoute createTravelRoute(){
        System.out.println("Input your Starting Bus Station.");
        String startStation = sc.nextLine();
        for (stationName : Station){
            if (startStation.equals(station.getName())){
                continue;
            }
            else{
                System.out.println("Invalid Starting Location. Please try again.");
                return null;
            }
        }
        System.out.println("What is your Bus Station destination.");
        String endStation = sc.nextLine();
        for (endStation : Station){
            if (endStation.equals(station.getName())){
                continue;
            }
            else {return null;}
        }
        return new travelRoute(startStation, endStation);
    }
    //public travelRoute createTravelRoute(){
        //System print: Input starting location
        //Scanner equals new Scanner: startInput
        //for (station in stationList):
            //if startInput equals station.getName():
                //continue
            //else:
                //System print: Invalid starting location. Please try again.
                //return null
        //System print: Input destination
        //Scanner equals new Scanner: endInput
        //for (station in stationList):
            //if endInput equals station.getName():
                //continue
            //else:
                //System print: Invalid starting location. Please try again.
                //return null
        //return new travelRoute(startInput, endInput)
/* ----------------------------User Subclasses---------------------------- */

/* Subclass for station manager(s) that extends User */
    public class stationManager extends User {
        private int sManagerID; //Unique identifier for station manager
        // public Station[] stationList; //List of stations managed by station manager
        public stationManager() {
        }

        public stationManager(String name, String username, String password) {
            super(name, username, password, perms);
        }
        /* Getter and Setter for sManagerID */
        public int getSManagerID() {
            return sManagerID;
        }
        public void setSManagerID(int sManagerID) {
            this.sManagerID = sManagerID;
        }
/*  ----------------------------StationManager methods---------------------------- */
        //private void addStation(){
            //System print: Input station name
            //Scanner equals new Scanner: sName

            //System print: Input station longitude
            //Scanner equals new Scanner: sLong

            //System print: Input station latitude
            //Scanner equals new Scanner: sLat

            //stationList.add(new Station(sName, sLong, sLat))
        //}

        //private void removeStation(){
            //System print: Input station name
            //Scanner equals new Scanner: sName

            //for (station in stationList):
                //if sName equals station.getName():
                    //stationList.remove(station)
                    //break
                //else:
                    //System print: Invalid station name. Please try again.
        //}

        //private void updateStation(){
            //System print: Input station's current name
            //Scanner equals new Scanner: sName

            //for (station in stationList):
                //if sName equals station.getName():
                    //System print: Input new station name (or press enter to keep current name)
                    //Scanner equals new Scanner: newSName
                    //if newSName is not empty:
                        //station.setName(newSName)

                    //System print: Input new station longitude (or press enter to keep current longitude)
                    //Scanner equals new Scanner: newSLong
                    //if newSLong is not empty:
                        //station.setLongitude(newSLong)

                    //System print: Input new station latitude (or press enter to keep current latitude)
                    //Scanner equals new Scanner: newSLat
                    //if newSLat is not empty:
                        //station.setLatitude(newSLat)
                //else:
                    //System print: Invalid station name. Please try again.
        //}

        //private void viewStations(){
            //for (station in stationList):
                //System print: station.getName() + " - Longitude: " + station.getLongitude() + ", Latitude: " + station.getLatitude()
        //}

        //private void viewStation(){
            //System print: Input station name
            //Scanner equals new Scanner: sName

            //for (station in stationList):
                //if sName equals station.getName():
                    //System print: station.getName() + " - Longitude: " + station.getLongitude() + ", Latitude: " + station.getLatitude()
                    //break
                //else:
                    //System print: Invalid station name. Please try again.
        //}
    }
}