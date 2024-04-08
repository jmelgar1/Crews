package org.ovclub.crews.utilities;

import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.List;

public class JsonUtilities {

    public List<String> JsonArrayToStringList(JsonArray jsonArray){
        List<String> stringList = new ArrayList<>();
        for(int i = 0; i < jsonArray.size(); i++){
            stringList.add(jsonArray.get(i).getAsString());
        }

        return stringList;
    }
}
