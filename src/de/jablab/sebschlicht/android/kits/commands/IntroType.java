package de.jablab.sebschlicht.android.kits.commands;

public enum IntroType {

    SHORT("short"),

    FULL("full");

    private String identifier;

    private IntroType(
            String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public static IntroType parseString(String type) {
        if (SHORT.getIdentifier().equals(type)) {
            return SHORT;
        } else if (FULL.getIdentifier().equals(type)) {
            return FULL;
        }
        System.out.println("unknown intro type \"" + type + "\"");

        return null;
    }
}
