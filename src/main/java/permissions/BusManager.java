package permissions;

import java.util.List;

import json.JsonUtilities;
import primary.Bus;
import primary.User;

public class BusManager extends User {
    private static BusManager instance = new BusManager();
    //Created Arraylist for buses
    private static List<Bus> buses = JsonUtilities.loadBuses();
    
    private BusManager(){}; //default constructor

    //contructor to inherit the basic user class, auto set perms to "busManager"
    public BusManager(String name, String username, String password, String perms) {
            super(name, username, password, "busManager");
    }

    //get the current instance of manager
    public static BusManager getInstance(){
        return instance;
    }

    //get list of buses
    public static List<Bus> getBuses(){
        return buses;
    }

    //find a bus based on the given ID
    public static Bus getBusByID(int id){
        for (Bus s : buses){
            if(s.getBusID() == id) return s;
        }
        return null;
    }

    //add bus to list and save to json
    public static void addBus(Bus bus) {
        buses.add(bus);
        JsonUtilities.saveBuses(buses);
    }

    //find bus in list, when id matches given bus, delete from list
    public static void deleteBus(Bus bus){
        for (int i = 0; i < buses.size(); i++) {
            if (buses.get(i).getBusID() == (bus.getBusID())) {
                buses.remove(i);
                break;
            }
        }
        JsonUtilities.saveBuses(buses);
    }

    //find bus in list, when id matches given bus, update bus with new bus
    public static void updateBus(Bus updatedBus) {
        for (int i = 0; i < buses.size(); i++) {
            if (buses.get(i).getBusID() == (updatedBus.getBusID())) {
                buses.set(i, updatedBus);
                break;
            }
        }

        JsonUtilities.saveBuses(buses);
    }
}
