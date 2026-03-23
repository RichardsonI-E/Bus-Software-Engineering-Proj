import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Station{
    //Imported Scanner as sc
    Scanner sc = new Scanner(System.in);
    //Created Arraylist for stations
    List<Station> stations = new ArrayList<>();

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
        public RefuelStation(String name, float longitude, float latitude){
            super(name, longitude, latitude);
        }
        //Method for refueling a bus.
        public void refuelBus(Bus bus){
            //Do we want to incorporate the get bus id from the bus class.
            System.out.println("Bus has been refueled. \n Bus ID: " + bus.getBusId());
        }
    }
}