package main;

import java.util.Scanner;

public class Bus {

    //Imported Scanner as sc
    Scanner sc = new Scanner(System.in);
    

    //Declaring Attributes
    private int busID; //ID number of Bus
    private String make; //Make of the Bus
    private String model; //Model of Bus
    private boolean fuelType;
    private float tankSize; //Size of Tank in gallons
    private float fuelBurnRate; //Fuel burn rate in MPG
    private float cruiseSpeed; // Most effiecent speed

    //Declare Constructor for Bus Class
    public Bus(int busID, String make, String model, boolean fuelType, float tankSize, float fuelBurnRate, float cruiseSpeed){
        this.busID = busID;
        this.make = make;
        this.model = model;
        this.fuelType = fuelType;
        this.tankSize = tankSize;
        this.fuelBurnRate = fuelBurnRate;
        this.cruiseSpeed = cruiseSpeed;
    }

        //------------------------------Setter and getter methods-------------------//
        public void setBusID(int busID){
            this.busID = busID;
        }
        public int getBusId(){
            return busID;
        }

            //set/get make
        public void setMake(String make){
            this.make = make;
        }
        public String getMake(){
            return make;
        }

            //set/get model
        public void setModel(String model){
            this.model = model;
        }
        public String getModel(){
            return model;
        }

            //set/get fuelType
        public void setFuelType(boolean fuelType){
            this.fuelType = fuelType;
        }
        public boolean getFuelType(){
            return fuelType;
        }

            //set/get tankSize
        public void setTankSize(float tankSize){
                this.tankSize = tankSize;
            }
        public float getTankSize(){
                return tankSize;
        }

            //set/get fuelBurnRate
        public void setFuelBurnRate(float fuelBurnRate){
            this.fuelBurnRate = fuelBurnRate;
        }
        public float getFuelBurnRate(){
            return fuelBurnRate;
        }

        //set/get cruiseSpeed
        public void setCruiseSpeed(float cruiseSpeed){
            this.cruiseSpeed = cruiseSpeed;
        }
        public float getCruiseSpeed(){
            return cruiseSpeed;
        }

        /*-------------------- Declare Subclass ----------------------*/
        public class CityBus extends Bus{
            public CityBus(int busID, String make, String model, float tankSize, float fuelBurnRate){
                super(busID, make, model, fuelType, tankSize, fuelBurnRate, cruiseSpeed);
            }
        }

        public class LongDisBus extends Bus{
            public LongDisBus(int busID, String make, String model, float tankSize, float fuelBurnRate){
                super(busID, make, model, fuelType, tankSize, fuelBurnRate, fuelBurnRate);
            }
        }

        //quick test of main class constructor
    public static void main(String[] args) {
        System.out.println("test test");
        Bus bus = new Bus(1234, "Travel", "Cross-Road",true, 80, 10, 55);
        System.out.println(bus.getBusId() + " " + bus.getMake() + " " + bus.getModel());
    }
}