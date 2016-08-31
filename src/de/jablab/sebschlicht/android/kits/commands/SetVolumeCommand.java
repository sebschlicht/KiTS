package de.jablab.sebschlicht.android.kits.commands;

import org.json.simple.JSONObject;

public class SetVolumeCommand extends Command {

    protected static final String FIELD_VOLUME = "volumeType";

    private int volume;

    public SetVolumeCommand(
            int volume) {
        super(CommandType.SET_VOLUME);
        this.volume = volume;
    }

    public int getVolume() {
        return volume;
    }

    @SuppressWarnings("unchecked")
    @Override
    public String toJson() {
        JSONObject command = super.toJsonObject();
        command.put(FIELD_VOLUME, String.valueOf(volume));

        return command.toJSONString();
    }

    protected static int loadVolume(JSONObject jsonObject) {
        return Integer.valueOf((String) jsonObject.get(FIELD_VOLUME));
    }

    public static Command parseJson(JSONObject jsonObject) {
        int volume = loadVolume(jsonObject);

        return new SetVolumeCommand(volume);
    }
}
