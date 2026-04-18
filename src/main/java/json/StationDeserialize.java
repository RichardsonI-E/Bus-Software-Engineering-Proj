
package json;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import primary.Station;
import primary.Station.BusStation;
import primary.Station.RefuelStation;
/*deserialization class to translate stations into bus or refuel subclasses
based on the "type" attribute*/
public class StationDeserialize implements JsonDeserializer<Station> {

        @Override
        public Station deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {

            //convert type from json element into string
            JsonObject obj = json.getAsJsonObject();
            String type = obj.get("type").getAsString();

            
            switch (type) {
                //if type is bus, make it a bus subclass
                case "bus" -> {
                    return context.deserialize(obj, BusStation.class);
                }
                //if type is refuel, make it a refuel subclass
                case "refuel" -> {
                    return context.deserialize(obj, RefuelStation.class);
                }
                default -> throw new JsonParseException("Unknown station type: " + type);
            }
        }
    }