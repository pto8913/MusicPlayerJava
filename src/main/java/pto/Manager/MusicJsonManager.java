package pto.Manager;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import pto.Controller.ListView.MusicData;
import pto.Controller.ListView.MusicDataAdapter;
import pto.Utils.ListUtils;

public class MusicJsonManager
{
    private final String fileName = "\\src\\main\\java\\pto\\Manager\\PtoMusicPlayerCache.json"; 
    private final String PLAYLIST = "PlayList";
    private final String MUSICLIST = "MusicList";
    private final String FILENAME = "fileName";
    private final String VOLUME = "Volume";

    private Map<String, List<MusicData>> cachedPlayList = new HashMap<>();

    // ----------------------------
    // Volume Functions
    // ----------------------------
    public void setVolume(double inVolume)
    {
        final String path = getPath();
        if (isExistsPath(path))
        {
            JsonObject json = getMusicDataJson(path);
            json.addProperty(VOLUME, (double)(int)inVolume);

            updateFile(path, json);
        }
    }
    public double getVolume()
    {
        final String path = getPath();
        if (isExistsPath(path))
        {
            JsonObject json = getMusicDataJson(path);
            if (json.has(VOLUME))
            {
                return json.get(VOLUME).getAsNumber().doubleValue();
            }
        }
        return 0;
    }

    // ----------------------------
    // PlayList Functions
    // ----------------------------
    public void addPlayList(MusicData playList)
    {
        final String path = getPath();
        JsonObject json = null;
        JsonArray playListJsonArray = new JsonArray();
        if (isExistsPath(path))
        {
            json = getMusicDataJson(path);
            if (json != null)
            {
                playListJsonArray = getPlayListJsonArray(json);
            }
        }
        if (json == null)
        {
            json = new JsonObject();
        }
        Gson gson = getMusicDataGson();
        JsonObject newJsonObject = gson.toJsonTree(playList).getAsJsonObject();
        newJsonObject.add(MUSICLIST, gson.toJsonTree(new ArrayList<>()));
        playListJsonArray.add(newJsonObject);

        updatePlayList(path, json, playListJsonArray);

        if (!cachedPlayList.containsKey(playList.getName()))
        {
            cachedPlayList.put(playList.getName(), new ArrayList<>());
        }
    }
    public void removePlayList(MusicData playList)
    {
        final String path = getPath();
        if (isExistsPath(path))
        {
            final JsonObject json = getMusicDataJson(path);
            final JsonArray playListJsonArray = getPlayListJsonArray(json);
            for (JsonElement playListJson : playListJsonArray)
            {
                JsonObject playJsonObject = playListJson.getAsJsonObject();
                if (playJsonObject.get(FILENAME).getAsString().equals(playList.getName()))
                {
                    playListJsonArray.remove(playJsonObject);
                    break;
                }
            }
            updatePlayList(path, json, playListJsonArray);

            if (cachedPlayList.containsKey(playList.getName()))
            {
                cachedPlayList.remove(playList.getName());
            }
        }
    }
    
    public void musicAddToPlayList(String targetPlayList, MusicData inPlayListItems)
    {
        final String path = getPath();
        if (isExistsPath(path))
        {
            final JsonObject json = getMusicDataJson(path);
            final JsonArray playListJsonArray = getPlayListJsonArray(json);
            for (JsonElement playListJson : playListJsonArray)
            {
                JsonObject playListJsonObj = playListJson.getAsJsonObject();
                if (playListJsonObj.get(FILENAME).getAsString().equals(targetPlayList))
                {
                    JsonArray musicListInPlayList = playListJsonObj.get(MUSICLIST).getAsJsonArray();
                    musicListInPlayList.add(toJson(inPlayListItems));
                    playListJsonObj.add(MUSICLIST, musicListInPlayList);
                    cachedPlayList.get(targetPlayList).add(inPlayListItems);

                    updatePlayList(path, json, playListJsonArray);
                    break;
                }
            }
        }
    }
    public void musicReplacePlayList(String playListName, int from, int to)
    {
        final String path = getPath();
        if (isExistsPath(path))
        {
            final JsonObject json = getMusicDataJson(path);
            final JsonArray playListJsonArray = getPlayListJsonArray(json);

            JsonObject fromJsonObject = new JsonObject();
            JsonObject toJsonObject = new JsonObject();
            for (JsonElement playListJson : playListJsonArray)
            {
                JsonObject playListJsonObj = playListJson.getAsJsonObject();
                if (playListJsonObj.get(FILENAME).getAsString().equals(playListName))
                {
                    JsonArray musicListInPlayList = playListJsonObj.get(MUSICLIST).getAsJsonArray();
                    fromJsonObject = musicListInPlayList.get(from).getAsJsonObject();
                    toJsonObject = musicListInPlayList.get(to).getAsJsonObject();
                    musicListInPlayList.set(to, fromJsonObject);
                    musicListInPlayList.set(from, toJsonObject);

                    updatePlayList(path, json, playListJsonArray);
                    break;
                }
            }
        }
    }
    public void musicRemoveFromPlayList(String targetPlayList, MusicData inPlayListItems)
    {
        final String path = getPath();
        if (isExistsPath(path))
        {
            final JsonObject json = getMusicDataJson(path);
            JsonArray playListJsonArray = getPlayListJsonArray(json);
            for (JsonElement playListJson : playListJsonArray)
            {
                JsonObject playListJsonObj = playListJson.getAsJsonObject();
                if (playListJsonObj.get(FILENAME).getAsString().equals(targetPlayList))
                {
                    Gson gson = getMusicDataGson();
                    JsonArray musicListInPlayList = playListJsonObj.get(MUSICLIST).getAsJsonArray();
                    for (JsonElement musicJsonElement : musicListInPlayList)
                    {
                        JsonObject musicJsonObject = musicJsonElement.getAsJsonObject();
                        MusicData musicData = gson.fromJson(musicJsonObject, MusicData.class);
                        if (musicData.equals(inPlayListItems))
                        {
                            musicListInPlayList.remove(musicJsonObject);
                            
                            playListJsonArray.remove(playListJsonObj);
                            playListJsonArray.add(playListJson);
                            
                            updatePlayList(path, json, playListJsonArray);

                            cachedPlayList.get(targetPlayList).remove(inPlayListItems);
                            return;
                        }
                    }
                }
            }
        }
    }

    // ----------------------------
    // Utility Functions
    // ----------------------------
    public boolean isExistsPath(String path)
    {
        return Files.exists(Paths.get(path));
    }
    private void updatePlayList(String path, JsonObject json, JsonArray playListJsonArray)
    {
        json.add(PLAYLIST, playListJsonArray);
        updateFile(path, json);
    }
    private void updateFile(String path, JsonObject json)
    {
        try
        {
            FileWriter fileWriter = new FileWriter(path);
            fileWriter.write(json.toString());
            fileWriter.flush();
            fileWriter.close();
        }
        catch (Exception e)
        {
        }
    }

    public String getCurrentPath()
    {
        try
        {
            return new java.io.File(".").getCanonicalPath();
        }
        catch (IOException e)
        {
            System.err.println(e);
        }
        return "";
    } 
    private String getPath()
    {
        return getCurrentPath() + fileName;
    }

    private <T> List<T> toList(Gson gson, JsonArray jsonArray)
    {
        Type listType = new TypeToken<List<T>>() {}.getType();
        return gson.fromJson(jsonArray, listType);
    }
    private List<MusicData> toMusicList(JsonArray jsonArray)
    {
        Gson gson = getMusicDataGson();
        Type listType = new TypeToken<List<MusicData>>() {}.getType();
        return gson.fromJson(jsonArray, listType);
    }
    private <T> JsonArray toJsonArray(List<T> items)
    {
        Gson gson = getMusicDataGson();
        return gson.toJsonTree(items).getAsJsonArray();
    }
    private <T> JsonObject toJson(T item)
    {
        Gson gson = getMusicDataGson();
        return gson.toJsonTree(item).getAsJsonObject();
    }

    private JsonObject getMusicDataJson(final String path)
    {
        try
        {
            com.google.gson.JsonParser parser = new com.google.gson.JsonParser();
            JsonElement parsedJson = parser.parse(new FileReader(path));
            if (!parsedJson.isJsonNull())
            {
                return parsedJson.getAsJsonObject();
            }
        }
        catch (FileNotFoundException e)
        {
        }
        return null;
    }    
    private Gson getMusicDataGson()
    {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(MusicDataAdapter.class, new MusicDataAdapter());
        return builder.create();
    }
    private JsonArray getPlayListJsonArray(JsonObject json)
    {
        if (json != null)
        {
            JsonElement elem = json.get(PLAYLIST);
            if (elem == null)
            {
                JsonArray newArray = new JsonArray();
                json.add(PLAYLIST, newArray);
                try
                {
                    updateFile(getPath(), json);
                }
                catch (Exception e)
                {
                }
                return newArray;
            }
            return elem.getAsJsonArray();
        }
        return new JsonArray();
    }
    private JsonArray getPlayListJsonArray() throws FileNotFoundException, IOException
    {
        final String path = getPath();
        if (isExistsPath(path))
        {
            JsonObject obj = getMusicDataJson(path);
            if (obj != null)
            {
                return getPlayListJsonArray(obj);
            }
        }
        return new JsonArray();
    }
    public List<MusicData> getAllPlayList() throws FileNotFoundException, IOException
    {
        if (!cachedPlayList.keySet().isEmpty())
        {
            return ListUtils.StringToMusicList(cachedPlayList.keySet());
        }
        JsonArray jsonArray = getPlayListJsonArray();
        if (jsonArray != null)
        {
            List<MusicData> out = toMusicList(jsonArray);
            for (MusicData elem : out)
            {
                cachedPlayList.put(elem.getName(), getAllMusicList(elem.getName()));
            }
            return out;
        }
        return null;
    }

    public JsonArray getMusicListJsonArray(String playListName) throws FileNotFoundException, IOException
    {
        if (cachedPlayList.containsKey(playListName))
        {
            return toJsonArray(cachedPlayList.get(playListName));
        }
        JsonArray playListJsonArray = getPlayListJsonArray();
        for (JsonElement playListJson : playListJsonArray)
        {
            JsonObject playListJsonObj = playListJson.getAsJsonObject();
            if (playListJsonObj.get(FILENAME).getAsString().equals(playListName))
            {
                return playListJsonObj.get(MUSICLIST).getAsJsonArray();
            }
        }
        return null;
    }
    public List<MusicData> getAllMusicList(String playListName) throws FileNotFoundException, IOException
    {
        if (cachedPlayList.containsKey(playListName))
        {
            return cachedPlayList.get(playListName);
        }
        final JsonArray musicListJsonArray = getMusicListJsonArray(playListName);
        if (musicListJsonArray != null)
        {
            List<MusicData> out = toMusicList(musicListJsonArray);
            cachedPlayList.put(playListName, out);
            return out;
        }
        return new ArrayList<>();
    }
    public boolean containMusicInPlayList(String playListName, String musicName)
    {
        if (cachedPlayList.containsKey(playListName))
        {
            return cachedPlayList.get(playListName).contains(new MusicData(musicName));
        }
        return false;
    }
}
