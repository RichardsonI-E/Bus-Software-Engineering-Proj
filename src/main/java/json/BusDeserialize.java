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

public class BusDeserialize implements JsonDeserializer<Bus> {

        @Override
        public Bus deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {

            JsonObject obj = json.getAsJsonObject();
            String type = obj.get("fuelType").getAsString();

            switch (type) {
                case "unleaded" -> {
                    return context.deserialize(obj, CityBus.class);
                }
                case "diesel" -> {
                    return context.deserialize(obj, LongDisBus.class);
                }
                default -> throw new JsonParseException("Unknown bus type: " + type);
            }
        }
    }
