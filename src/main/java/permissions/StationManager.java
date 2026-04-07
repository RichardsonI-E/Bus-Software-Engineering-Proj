package permissions;

import java.util.List;

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

    public static void addStation(Station bus) {
        stations.add(bus);
        JsonUtilities.saveStations(stations);
    }

    public static void deleteStation(Station station){
        for (int i = 0; i < stations.size(); i++) {
            if (stations.get(i).getName().equals(station.getName())) {
                stations.remove(i);
                break;
            }
        }
        JsonUtilities.saveStations(stations);
    }

    public static void updateStation(Station updatedStation) {
        for (int i = 0; i < stations.size(); i++) {
            if (stations.get(i).getName().equals(updatedStation.getName())) {
                stations.set(i, updatedStation);
                break;
            }
        }

        JsonUtilities.saveStations(stations);
    }

    public static void updateStation(Station updatedStation, String ogStationname) {
        for (int i = 0; i < stations.size(); i++) {
            if (stations.get(i).getName().equals(ogStationname)) {
                stations.set(i, updatedStation);
                break;
            }
        }

        JsonUtilities.saveStations(stations);
    }
}
