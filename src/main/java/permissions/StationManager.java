package permissions;

import java.util.List;

import javax.swing.JOptionPane;

import json.JsonUtilities;
import primary.Station;
import primary.User;



public class StationManager extends User{
    private static StationManager instance = new StationManager();
    //Created Arraylist for stations
    private static List<Station> stations = JsonUtilities.loadStations();
    
    private StationManager(){}; //default constructor

    public StationManager(String name, String username, String password, String perms) {
            super(name, username, password, "stationManager");
    }

    public static StationManager getInstance(){
        return instance;
    }

    public static List<Station> getStations(){
        return stations;
    }

    public static Station getStationByID(int id){
        for (Station s: stations){
            if(s.getID() == id) return s;
        }
        return null;
    }

    public static Station getStationByName(String name){
        for (Station s: stations){
            if(s.getName().equals(name)) return s;
        }
        return null;
    }

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

    public static void deleteStation(Station station){
        for (int i = 0; i < stations.size(); i++) {
            if (stations.get(i).getID() == (station.getID())) {
                stations.remove(i);
                break;
            }
        }
        JsonUtilities.saveStations(stations);
    }

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
