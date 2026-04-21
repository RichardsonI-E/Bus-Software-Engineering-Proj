package permissions;

import java.util.List;

import javax.swing.JOptionPane;

import json.JsonUtilities;
import primary.Station;
import primary.User;
/*
Station Manager subclass of user that manages the database of stations
The class holds all methods that have to do with modifying, saving and loading
the list of stations.
*/
public class StationManager extends User{
    private static StationManager instance = new StationManager();
    //Created Arraylist for stations
    private static List<Station> stations = JsonUtilities.loadStations();
    
    private StationManager(){}; //default constructor

    //contructor to inherit the basic user class, auto set perms to "stationManager"
    public StationManager(String name, String username, String password, String perms) {
            super(name, username, password, "stationManager");
    }

    //get the current instance of manager
    public static StationManager getInstance(){
        return instance;
    }

    //get list of stations
    public static List<Station> getStations(){
        return stations;
    }

    //find a station based on the given ID
    public static Station getStationByID(int id){
        for (Station s: stations){
            if(s.getID() == id) return s;
        }
        return null;
    }

    //find a station based on the given name
    public static Station getStationByName(String name){
        for (Station s: stations){
            if(s.getName().equals(name)) return s;
        }
        return null;
    }

    //check if a station already exists with the given name, if not add to list
    public static void addStation(Station newStation){
        boolean free = true;
        
        for (Station s: stations){
            if(s.getName().equals(newStation.getName())){
                free = false;
            }
        }

        if(free){
            stations.add(newStation);
        }else{
            JOptionPane.showMessageDialog(null,
            "A station already exists by that name. Please use a new name or alter the existing station",
            "Name Taken", JOptionPane.WARNING_MESSAGE);
        }
        JsonUtilities.saveStations(stations);
    }

    //find station based on ID, if matching station is found delete it
    public static void deleteStation(Station station){
        for (int i = 0; i < stations.size(); i++) {
            if (stations.get(i).getID() == (station.getID())) {
                stations.remove(i);
                break;
            }
        }
        JsonUtilities.saveStations(stations);
    }

    //find station based on ID, if matching station is found replace it with new
    public static void updateStation(Station updatedStation) {
        for (int i = 0; i < stations.size(); i++) {
            if (stations.get(i).getID() == (updatedStation.getID())) {
                stations.set(i, updatedStation);
                break;
            }
        }

        JsonUtilities.saveStations(stations);
    }
}
