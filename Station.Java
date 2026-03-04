class Station{
    //Declare private Attributes
    private String name; //Name Attribute of Station
    private float longtitude; //Longtitude Attribute for location
    private float latitude; //Latitude Attribute for location

    //Declare Constructor
    public Station(String name, float longtitude, float latitude){
        this.name = name;
        this.longtitude = longtitude;
        this.latitude = latitude; 
    }
    /*----------------- Setters and Getters for Attributes -----------------*/
    public void setName(String name){
        this.name = name; 
    }    
    public String getName(){
        return name; 
    }

    public void setLongtitude(float longtitude){
        this.longtitude = longtitude; 
    }
    public float getLongtitude(){
        return longtitude; 
    }

    public void setLatitude(float latitude){
        this.latitude = latitude;
    }
    public float getLatitude(){
        return latitude;
    }

    /*----------------- Station Subclass -----------------*/
    public class BusStation extends Station{

    }
    public class RefuelStation extends Station{
        public RefuelStation(String name, float longtitude, float latitude){
            super(name,longtitude,latitude);
        }
        //Method for refueling a bus.
        public void refuelBus(){
            //Do we want to incorporate the get bus id from the bus class.
            System.out.println("Bus has been refuel");
        }
    }
}