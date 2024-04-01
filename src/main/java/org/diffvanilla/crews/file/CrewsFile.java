package org.diffvanilla.crews.file;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.diffvanilla.crews.Crews;
import org.diffvanilla.crews.object.Crew;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

public class CrewsFile {
    private final Crews plugin;
    private File crewsFile;
    private JsonObject crewsData;
    public JsonObject getCrewsData() { return this.crewsData; }

    public CrewsFile(final Crews plugin){
        this.plugin = plugin;
    }

    public void loadCrews() {
        Gson gson = new Gson();
        crewsFile = new File(plugin.getDataFolder(), "crews.json");
        if (!crewsFile.exists()) {
            try {
                crewsFile.getParentFile().mkdirs();
                crewsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        try (FileReader reader = new FileReader(crewsFile)) {
            JsonObject crewsData;
            try {
                crewsData = JsonParser.parseReader(reader).getAsJsonObject();
            } catch (JsonSyntaxException e) {
                System.err.println("Error parsing JSON: " + e.getMessage());
                return;
            }
            if (crewsData == null) {
                System.err.println("No data found in crews.json");
                return;
            }
            for (Map.Entry<String, JsonElement> entry : crewsData.entrySet()) {
                Type type = new TypeToken<Map<String, Object>>() {}.getType();
                Map<String, Object> crewMap = gson.fromJson(entry.getValue(), type);
                Crew loadedCrew = Crew.deserialize(crewMap, plugin);
                plugin.getData().addCrew(loadedCrew);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void saveCrews() {
        System.out.println("Saving Crews");
        Gson gson = new Gson();
        JsonObject allCrewsJson = new JsonObject();
        for (Crew c : plugin.getData().getCrews()) {
            JsonObject crewJson = gson.toJsonTree(c.serialize()).getAsJsonObject();
            allCrewsJson.add(c.getName(), crewJson);
        }
        try (FileWriter file = new FileWriter(crewsFile)) {
            gson.toJson(allCrewsJson, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

