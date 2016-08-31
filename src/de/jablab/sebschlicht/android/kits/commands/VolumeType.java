package de.jablab.sebschlicht.android.kits.commands;

public enum VolumeType {

    DOWN("down"),

    UP("up");

    private String identifier;

    private VolumeType(
            String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public static VolumeType parseString(String type) {
        if (DOWN.getIdentifier().equals(type)) {
            return DOWN;
        } else if (UP.getIdentifier().equals(type)) {
            return UP;
        }
        System.out.println("unknown volume type \"" + type + "\"");

        return null;
    }
}
