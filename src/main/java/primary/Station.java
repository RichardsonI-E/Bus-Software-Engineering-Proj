package primary;

public class Station{
    //Declare private Attributes
    private String name; //Name Attribute of Station
    private float longitude; //Longitude Attribute for location
    private float latitude; //Latitude Attribute for location

    //Declare empty constructor
    public Station(){
        name = "";
        longitude = (float) 0.0;
        latitude = (float) 0.0;
    }

    //Declare Constructor
    public Station(String name, float longitude, float latitude){
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
    }
    /*----------------- Setters and Getters for Attributes -----------------*/
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }

    public void setLongitude(float longitude){
        this.longitude = longitude;
    }
    public float getLongitude(){
        return longitude;
    }

    public void setLatitude(float latitude){
        this.latitude = latitude;
    }
    public float getLatitude(){
        return latitude;
    }

    /*----------------- Station Subclass -----------------*/
    public class BusStation extends Station{
        public BusStation(String name, float longitude, float latitude){
            super(name, longitude, latitude);
        }
    }
    public class RefuelStation extends Station{
        private String fuelType;
        public RefuelStation(String name, float longitude, float latitude,String fuelType){
            super(name, longitude, latitude);
            this.fuelType = fuelType;
        }
        //Method for refueling a bus.
        public void refuelBus(Bus bus){
            //Do we want to incorporate the get bus id from the bus class.
            //add code that 'resets' the max range of the given bus to full
            System.out.println("Bus has been refueled. \n Bus ID: " + bus.getBusId());
        }
    }
}