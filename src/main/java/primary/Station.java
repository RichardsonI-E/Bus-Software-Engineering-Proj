package primary;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
/*This class is to define the main abstract station object and its two subclasses, refuel and
bus. The ID is determined by a random number from 1 to 1 million while
the other attributes are defined by its subclass or a manager's input
*/
public abstract class Station {
    //Declare private Attributes
    private String name; //Name Attribute of Station
    private float longitude; //Longitude Attribute for location
    private float latitude; //Latitude Attribute for location
    private int ID; //attribute to assign unique IDs to buses

    //abstract attribute to determine if station is bus or refuel
    public abstract String getType();
    

    //Declare empty constructor
    public Station() {
        name = "";
        longitude = (float) 0.0;
        latitude = (float) 0.0;
    }

    //Declare Constructor
    public Station(String name, float latitude, float longitude) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        //set ID to random number from 1 to 1 million on construction
        this.ID = ThreadLocalRandom.current().nextInt(1, 1000000);
    }

    /*----------------- Setters and Getters for Attributes -----------------*/
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    /*----------------- Station Subclass -----------------*/
    public static class BusStation extends Station {
        //declare station type as subclass attribute, auto set it to bus
        private String type;

        public BusStation(String name, float latitude, float longitude) {
            super(name, longitude, latitude);
            this.type = "bus";
        }
        /*----------------- Setters and Getters for Attributes -----------------*/
        @Override
        public String getType() {
            return "bus";
        }
    }

    public static class RefuelStation extends Station {
        //declare station type as subclass attribute, auto set it to refuel
        private String type;
        //create a list of fuel types the station supports
        private ArrayList<String> fuelType;

        public RefuelStation(String name, float latitude, float longitude, ArrayList<String> fuelType) {
            super(name, longitude, latitude);
            this.type = "refuel";
            this.fuelType = fuelType;
        }

        /*----------------- Setters and Getters for Attributes -----------------*/
        public void setFuelTypes(ArrayList<String> fuelType) {
            this.fuelType = fuelType;
        }

        public ArrayList<String> getFuelType() {
            return fuelType;
        }

        @Override
        public String getType() {
            return "refuel";
        }
    }
}
