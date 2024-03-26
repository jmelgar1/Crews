package org.diffvanilla.crews.file;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.diffvanilla.crews.Crews;
import org.diffvanilla.crews.object.Crew;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CrewsFile {
    private final Crews plugin;

    public CrewsFile(final Crews plugin){
        this.plugin = plugin;
    }

    private File crewsFile;
    private FileConfiguration crewsData;
    public void loadCrews(){
        crewsFile = new File(plugin.getDataFolder(),"crews.yml");
        if(!crewsFile.exists()){
            crewsFile.getParentFile().mkdir();
            try{
                crewsFile.createNewFile();
            } catch(IOException e){
                e.printStackTrace();
            }
        }
        crewsData = YamlConfiguration.loadConfiguration(crewsFile);
        if(!crewsData.getKeys(false).isEmpty()){
            for(String key : crewsData.getKeys(false)){
                Crew loadedCrew = Crew.deserialize(crewsData.getConfigurationSection(key).getValues(false), plugin);
                plugin.getData().addCrew(loadedCrew);
            }
        }
    }

    public void saveCrews(){
        System.out.println("Saving Crews");
        List<String> savedCrews = new ArrayList<>();

        for(Crew c : plugin.getData().getCrews()){
            crewsData.set(c.getName(), c.serialize());
            savedCrews.add(c.getName());
        }
        try {
            crewsData.save(crewsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(String key : crewsData.getKeys(false)){
            if(!savedCrews.contains(key)){
                crewsData.set(key, null);
            }
        }

        try {
            crewsData.save(crewsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

