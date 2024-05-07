package org.ovclub.crews.file;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.ovclub.crews.Crews;
import org.ovclub.crews.object.Crew;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

public class CrewsFile {
    private final Crews plugin;
    private File crewsFile;

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
                String uuid = entry.getKey();
                Type type = new TypeToken<Map<String, Object>>() {}.getType();
                Map<String, Object> crewMap = gson.fromJson(entry.getValue(), type);
                Crew loadedCrew = Crew.deserialize(uuid, crewMap, plugin);
                plugin.getData().addCrew(loadedCrew);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void saveCrews() {
        Gson gson = new GsonBuilder()
            .registerTypeAdapter(Crew.class, new CrewTypeAdapter())
            .create();

        JsonObject allCrewsJson = new JsonObject();
        for (Crew c : plugin.getData().getCrews()) {
            JsonObject crewJson = gson.toJsonTree(c).getAsJsonObject();
            if(c.getUuid() == null) {
                c.setUuid();
            }
            allCrewsJson.add(c.getUuid(), crewJson);
        }
        try (FileWriter file = new FileWriter(crewsFile)) {
            gson.toJson(allCrewsJson, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

