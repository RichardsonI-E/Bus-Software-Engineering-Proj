package primary;

import java.util.ArrayList;

public abstract class Station {

    //Declare private Attributes
    private String name; //Name Attribute of Station
    private float longitude; //Longitude Attribute for location
    private float latitude; //Latitude Attribute for location

    //abstract attribute to determine if station is bus or refuel
    public abstract String getType();

    //Declare empty constructor
    public Station() {
        name = "";
        longitude = (float) 0.0;
        latitude = (float) 0.0;
    }

    //Declare Constructor
    public Station(String name, float longitude, float latitude) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
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

    /*----------------- Station Subclass -----------------*/
    public static class BusStation extends Station {
        private String type;

        public BusStation(String name, float longitude, float latitude) {
            super(name, longitude, latitude);
            this.type = "bus";
        }
        @Override
        public String getType() {
            return "bus";
        }
    }

    public static class RefuelStation extends Station {
        private String type ;
        private ArrayList<String> fuelType;

        public RefuelStation(String name, float longitude, float latitude, ArrayList<String> fuelType) {
            super(name, longitude, latitude);
            this.type = "refuel";
            this.fuelType = fuelType;
        }

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

        //Method for refueling a bus.
        public void refuelBus(Bus bus) {
            //Do we want to incorporate the get bus id from the bus class.
            //add code that 'resets' the max range of the given bus to full
            System.out.println("Bus has been refueled. \n Bus ID: " + bus.getBusId());
        }
    }
}
