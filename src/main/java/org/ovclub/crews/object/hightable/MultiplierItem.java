package org.ovclub.crews.object.hightable;

import java.util.LinkedHashMap;

public class MultiplierItem {
    private String section;
    private LinkedHashMap<String, Double> values;

    public MultiplierItem(String section, LinkedHashMap<String, Double> values) {
        this.section = section;
        this.values = values;
    }

    public String getSection() {
        return this.section;
    }

    public LinkedHashMap<String, Double> getValues() {
        return this.values;
    }
}
