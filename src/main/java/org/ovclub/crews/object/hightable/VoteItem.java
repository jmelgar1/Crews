package org.ovclub.crews.object.hightable;

public class VoteItem {
    private String section;
    private String item;
    private String multiplier;

    public VoteItem(String section, String item, String multiplier) {
        this.section = section;
        this.item = item;
        this.multiplier = multiplier;
    }

    // Getters
    public String getSection() {
        return section;
    }

    public String getItem() {
        return item;
    }

    public String getMultiplier() {
        return multiplier;
    }
}
