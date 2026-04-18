package primary;

import java.util.concurrent.ThreadLocalRandom;

public abstract class Bus {
    
    //Declaring Attributes
    private int busID; //ID number of Bus
    private String make; //Make of the Bus
    private String model; //Model of Bus
    private float tankSize; //Size of Tank in gallons
    private float fuelBurnRate; //Fuel burn rate in MPG
    private float cruiseSpeed; // Most effiecent speed (miles per hour)

    //abstract attribute to determine if bus takes diesel or unleaded
    public abstract String getFuel();

    //Declare Constructor for Bus Class
    public Bus(String make, String model, float tankSize, float fuelBurnRate, float cruiseSpeed){
        //automatically set the id of the bus to a number from 1 to 1 million
        this.busID = ThreadLocalRandom.current().nextInt(1, 1000000);
        this.make = make;
        this.model = model;
        this.tankSize = tankSize;
        this.fuelBurnRate = fuelBurnRate;
        this.cruiseSpeed = cruiseSpeed;
    }

        //------------------------------Setter and getter methods-------------------//
        //set/get bus ID#
        public void setBusID(int busID){
            this.busID = busID;
        }
        public int getBusID(){
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

        //calculate bus' max range
        public float calcMaxRange(){
            return tankSize/fuelBurnRate;
        }

        /*-------------------- Declare Subclass ----------------------*/
        //declare fuel type as subclass attribute, auto set it to unleaded
        public static class CityBus extends Bus{
            private String fuelType;
            public CityBus(String make, String model, float tankSize, float fuelBurnRate, float cruiseSpeed){
                super(make, model, tankSize, fuelBurnRate, cruiseSpeed);
                this.fuelType = "unleaded";
            }
            @Override
            public String getFuel() {
            return "unleaded";
            }
        }

        //declare fuel type as subclass attribute, auto set it to diesel
        public static class LongDisBus extends Bus{
            private String fuelType;
            public LongDisBus(String make, String model, float tankSize, float fuelBurnRate, float cruiseSpeed){
                super(make, model, tankSize, fuelBurnRate, cruiseSpeed);
                this.fuelType = "diesel";
            }
            @Override
            public String getFuel() {
            return "diesel";
            }
        }
}