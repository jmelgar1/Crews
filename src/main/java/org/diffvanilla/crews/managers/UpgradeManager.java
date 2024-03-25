package org.diffvanilla.crews.managers;

import com.google.gson.JsonObject;
import org.diffvanilla.crews.Crews;

public class UpgradeManager {
    //Crews instance
    private final Crews crewsClass = Crews.getInstance();

    public boolean checkForUpgrade(String crew, String upgrade){
        addUpgradesSection(crew);

        JsonObject crewsJson = crewsClass.getcrewsJson();
        JsonObject crewObject = crewsJson.getAsJsonObject(crew);
        JsonObject upgradeObject = crewObject.getAsJsonObject("tribal_upgrades");

        if(upgradeObject.get(upgrade) == null){
            upgradeObject.addProperty(upgrade, false);
        }

        return upgradeObject.get(upgrade).getAsBoolean();
    }

    public void addUpgradesSection(String crew){
        JsonObject crewsJson = crewsClass.getcrewsJson();
        JsonObject crewObject = crewsJson.getAsJsonObject(crew);

        if(crewObject.getAsJsonObject("tribal_upgrades") == null) {
            JsonObject upgradesSection = new JsonObject();
            upgradesSection.addProperty("chat", false);
            upgradesSection.addProperty("discord", false);
            upgradesSection.addProperty("mail", false);
            crewObject.add("tribal_upgrades", upgradesSection);
            crewsClass.savecrewsFileJson();
        }
    }

    public void addUpgradesSection(){
        JsonObject crewsJson = crewsClass.getcrewsJson();

        try {
            for (String crew : crewsJson.keySet()) {
                JsonObject crewObject = crewsJson.getAsJsonObject(crew);
                JsonObject upgradesSection = new JsonObject();
                upgradesSection.addProperty("chat", false);
                upgradesSection.addProperty("discord", false);
                upgradesSection.addProperty("mail", false);
                crewObject.add("tribal_upgrades", upgradesSection);
            }

            crewsClass.savecrewsFileJson();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void editUpgrade(String crew, String upgrade, boolean bool){
        JsonObject crewsJson = crewsClass.getcrewsJson();
        JsonObject crewObject = crewsJson.getAsJsonObject(crew);
        JsonObject upgradeObject = crewObject.getAsJsonObject("tribal_upgrades");

        if(upgradeObject.get(upgrade) == null){
            upgradeObject.addProperty(upgrade, false);
        }

        upgradeObject.addProperty(upgrade, bool);
        crewsClass.savecrewsFileJson();
    }
}
