
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

public class StationDeserialize implements JsonDeserializer<Station> {

        @Override
        public Station deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {

            JsonObject obj = json.getAsJsonObject();
            String type = obj.get("type").getAsString();

            switch (type) {
                case "bus":
                    return context.deserialize(obj, BusStation.class);
                case "refuel":
                    return context.deserialize(obj, RefuelStation.class);
                default:
                    throw new JsonParseException("Unknown station type: " + type);
            }
        }
    }