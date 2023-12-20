package pto.Controller.ListView;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class MusicDataAdapter implements JsonSerializer<MusicData>, JsonDeserializer<MusicData>
{
    @Override
    public MusicData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        JsonObject jsonObject = json.getAsJsonObject();
        MusicData data = new MusicData();
        data.setName(jsonObject.get("name").getAsString());
        data.setPath(jsonObject.get("path").getAsString());
        return data;
    }

    @Override
    public JsonElement serialize(MusicData src, Type typeOfSrc, JsonSerializationContext context)
    {
        JsonObject obj = new JsonObject();
        obj.addProperty("name", src.getName());
        obj.addProperty("path", src.getPath());
        return obj;
    }  
}