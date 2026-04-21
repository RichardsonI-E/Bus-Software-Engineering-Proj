package json;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import primary.Bus;
import primary.Bus.CityBus;
import primary.Bus.LongDisBus;
/*deserialization class to translate buses into long distance or city
subclass based on the fuel types it takes*/
public class BusDeserialize implements JsonDeserializer<Bus> {
        @Override
        public Bus deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {

            JsonObject obj = json.getAsJsonObject();
            String type = obj.get("fuelType").getAsString();

            //if bus takes unleaded, it is city, otherwise it is long distance
            switch (type) {
                case "unleaded" -> {
                    return context.deserialize(obj, CityBus.class);
                }
                case "diesel" -> {
                    return context.deserialize(obj, LongDisBus.class);
                }
                //if fuel type doesn't match the 2, throw exception
                default -> throw new JsonParseException("Unknown bus type: " + type);
            }
        }
    }
