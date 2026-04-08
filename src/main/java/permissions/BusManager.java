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

    public BusManager(String name, String username, String password, String perms) {
            super(name, username, password, "busManager");
    }

    public static BusManager getInstance(){
        return instance;
    }

    public static List<Bus> getBuses(){
        return buses;
    }

    public static Bus getBusByID(int id){
        for (Bus s : buses){
            if(s.getBusID() == id) return s;
        }
        return null;
    }

    public static void addBus(Bus bus) {
        buses.add(bus);
        JsonUtilities.saveBuses(buses);
    }

    public static void deleteBus(Bus station){
        for (int i = 0; i < buses.size(); i++) {
            if (buses.get(i).getBusID() == (station.getBusID())) {
                buses.remove(i);
                break;
            }
        }
        JsonUtilities.saveBuses(buses);
    }

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
