public class Bus {
    //Declaring Attributes
    private int busID;
    private String make;
    private String model;
    private float tankSize;
    private float fuelBurnRate;
    private float cruiseSpeed;

    //Current Methods
        //set/get busID
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


    public static void main(String[] args) {
        System.out.println("test test");
    }
}
