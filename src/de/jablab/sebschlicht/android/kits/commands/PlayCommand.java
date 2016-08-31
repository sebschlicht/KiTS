package de.jablab.sebschlicht.android.kits.commands;

import org.json.simple.JSONObject;

public class PlayCommand extends Command {

    protected static final String FIELD_NAME = "name";

    protected static final String FIELD_INTRO_TYPE = "introType";

    private String name;

    private IntroType type;

    public PlayCommand(
            String name,
            IntroType type) {
        super(CommandType.PLAY);
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public IntroType getType() {
        return type;
    }

    @SuppressWarnings("unchecked")
    @Override
    public String toJson() {
        JSONObject command = super.toJsonObject();
        command.put(FIELD_NAME, name);
        command.put(FIELD_INTRO_TYPE, type.getIdentifier());

        return command.toJSONString();
    }

    protected static String loadName(JSONObject jsonObject) {
        return (String) jsonObject.get(FIELD_NAME);
    }

    protected static IntroType loadIntroType(JSONObject jsonObject) {
        return IntroType.parseString((String) jsonObject.get(FIELD_INTRO_TYPE));
    }

    public static Command parseJson(JSONObject jsonObject) {
        String name = loadName(jsonObject);
        IntroType introType = loadIntroType(jsonObject);

        return new PlayCommand(name, introType);
    }
}
