package org.ovclub.crews.file;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.bukkit.Location;
import org.ovclub.crews.object.Crew;

import java.io.IOException;
import java.util.Map;

class CrewTypeAdapter extends TypeAdapter<Crew> {
    private final Gson localGson;

    public CrewTypeAdapter() {
        this.localGson = new Gson();
    }

    @Override
    public void write(JsonWriter out, Crew crew) throws IOException {
        out.beginObject();
        out.name("name").value(crew.getName());
        out.name("level").value(crew.getLevel());
        out.name("dateFounded").value(crew.getDateFounded());
        out.name("vault").value(crew.getVault());
        out.name("boss").value(crew.getBoss());
        writeComplexObject(out, "enforcers", crew.getEnforcers());
        out.name("levelUpCost").value(crew.getLevelUpCost());
        out.name("memberLimit").value(crew.getMemberLimit());
        writeComplexObject(out, "members", crew.getMembers());
        out.name("description").value(crew.getDescription());
        writeComplexObject(out, "compound", crew.getCompound());
        out.name("kills").value(crew.getKills());
        out.name("rating").value(crew.getRating());
        out.name("skirmishWins").value(crew.getSkirmishWins());
        out.name("skirmishDraws").value(crew.getSkirmishDraws());
        out.name("skirmishLosses").value(crew.getSkirmishLosses());
        out.name("influence").value(crew.getInfluence());
        writeComplexObject(out, "unlockedUpgrades", crew.getUnlockedUpgrades());
        writeComplexObject(out, "vaultDeposits", crew.getVaultDeposits());
        //writeComplexObject(out, "sentMail", crew.getSentMail());
        out.endObject();
    }

    @Override
    public Crew read(JsonReader in) {
        return null;
    }

    private void writeComplexObject(JsonWriter out, String name, Object obj) throws IOException {
        out.name(name);
        if (obj != null) {
            if (obj instanceof Location) {
                writeLocation(out, (Location) obj);
            } else {
                JsonElement jsonElement = localGson.toJsonTree(obj);
                writeJsonElement(out, jsonElement);
            }
        } else {
            out.nullValue();
        }
    }

    private void writeJsonElement(JsonWriter out, JsonElement element) throws IOException {
        if (element.isJsonNull()) {
            out.nullValue();
        } else if (element.isJsonObject()) {
            out.beginObject();
            for (Map.Entry<String, JsonElement> e : element.getAsJsonObject().entrySet()) {
                out.name(e.getKey());
                writeJsonElement(out, e.getValue());
            }
            out.endObject();
        } else if (element.isJsonArray()) {
            out.beginArray();
            for (JsonElement e : element.getAsJsonArray()) {
                writeJsonElement(out, e);
            }
            out.endArray();
        } else if (element instanceof JsonPrimitive){
            JsonPrimitive prim = element.getAsJsonPrimitive();
            if (prim.isBoolean()) {
                out.value(prim.getAsBoolean());
            } else if (prim.isString()) {
                out.value(prim.getAsString());
            } else if (prim.isNumber()) {
                out.value(prim.getAsNumber());
            }
        }
    }

    private void writeLocation(JsonWriter out, Location loc) throws IOException {
        out.beginObject();
        out.name("world").value(loc.getWorld() != null ? loc.getWorld().getName() : null);
        out.name("x").value(loc.getX());
        out.name("y").value(loc.getY());
        out.name("z").value(loc.getZ());
        out.name("yaw").value(loc.getYaw());
        out.name("pitch").value(loc.getPitch());
        out.endObject();
    }
}
