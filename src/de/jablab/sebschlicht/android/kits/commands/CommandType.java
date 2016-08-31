package de.jablab.sebschlicht.android.kits.commands;

public enum CommandType {

    SET_VOLUME("volume", SetVolumeCommand.class),

    PLAY("play", PlayCommand.class),

    REGISTER("register", RegisterCommand.class),

    STOP("stop", StopCommand.class);

    private String identifier;

    private Class<? extends Command> type;

    private CommandType(
            String identifier,
            Class<? extends Command> type) {
        this.identifier = identifier;
        this.type = type;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Class<? extends Command> getType() {
        return type;
    }

    public static CommandType parseString(String commandType) {
        if (SET_VOLUME.getIdentifier().equals(commandType)) {
            return SET_VOLUME;
        } else if (PLAY.getIdentifier().equals(commandType)) {
            return PLAY;
        } else if (REGISTER.getIdentifier().equals(commandType)) {
            return REGISTER;
        } else if (STOP.getIdentifier().equals(commandType)) {
            return STOP;
        }

        return null;
    }
}
