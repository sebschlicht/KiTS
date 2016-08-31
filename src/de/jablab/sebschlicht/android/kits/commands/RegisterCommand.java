package de.jablab.sebschlicht.android.kits.commands;

import org.json.simple.JSONObject;

public class RegisterCommand extends Command {

    public RegisterCommand() {
        super(CommandType.REGISTER);
    }

    @Override
    public String toJson() {
        JSONObject command = super.toJsonObject();
        return command.toJSONString();
    }

    public static Command parseJson(JSONObject jsonObject) {
        return new RegisterCommand();
    }
}
