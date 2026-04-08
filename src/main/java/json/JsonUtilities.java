package json;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import primary.Bus;
import primary.Station;
import primary.User;

/*This class is used to allow the program to load from and save to json files for users, 
stations, and buses with Gson*/
public class JsonUtilities {

    // method to save stations to "stations.json"
    public static void saveStations(List<Station> stations) {
        Gson gson = new Gson();

        try (FileWriter writer = new FileWriter("src/main/java/json/stations.json")) {
            gson.toJson(stations, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // method to load stations from "stations.json"
    public static List<Station> loadStations() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Station.class, new StationDeserialize())
                .create();

        try (FileReader reader = new FileReader("src/main/java/json/stations.json")) {
            return gson.fromJson(reader, new TypeToken<List<Station>>() {}.getType());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // method to save users to "users.json"
    public static void saveUsers(List<User> users) {
        Gson gson = new Gson();

        try (FileWriter writer = new FileWriter("src/main/java/json/users.json")) {
            gson.toJson(users, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // method to load users from "users.json"
    public static List<User> loadUsers() {
        Gson gson = new Gson();

        try (FileReader reader = new FileReader("src/main/java/json/users.json")) {
            return gson.fromJson(reader, new TypeToken<List<User>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // method to save stations to "buses.json"
    public static void saveBuses(List<Bus> buses) {
        Gson gson = new Gson();

        try (FileWriter writer = new FileWriter("src/main/java/json/buses.json")) {
            gson.toJson(buses, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // method to load stations from "buses.json"
    public static List<Bus> loadBuses() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Bus.class, new BusDeserialize())
                .create();

        try (FileReader reader = new FileReader("src/main/java/json/buses.json")) {
            return gson.fromJson(reader, new TypeToken<List<Bus>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}

