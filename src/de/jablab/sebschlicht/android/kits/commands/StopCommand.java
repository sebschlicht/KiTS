package de.jablab.sebschlicht.android.kits.commands;

import org.json.simple.JSONObject;

public class StopCommand extends Command {

    public StopCommand() {
        super(CommandType.STOP);
    }

    @Override
    public String toJson() {
        JSONObject command = super.toJsonObject();
        return command.toJSONString();
    }

    public static Command parseJson(JSONObject jsonObject) {
        return new StopCommand();
    }
}
