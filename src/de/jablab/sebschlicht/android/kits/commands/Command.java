package de.jablab.sebschlicht.android.kits.commands;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public abstract class Command {

    private static final JSONParser PARSER = new JSONParser();

    public static final String SERVER_SEARCH_REQUEST_STRING = "WHEREISKITS";

    public static byte[] SERVER_SEARCH_REQUEST;
    static {
        SERVER_SEARCH_REQUEST = getUtf8Bytes(SERVER_SEARCH_REQUEST_STRING);
    }

    public static final String SERVER_SEARCH_RESPONSE_STRING = "HEREISKITS";

    public static byte[] SERVER_SEARCH_RESPONSE;
    static {
        SERVER_SEARCH_RESPONSE = getUtf8Bytes(SERVER_SEARCH_RESPONSE_STRING);
    }

    protected static final String FIELD_TYPE = "type";

    protected CommandType type;

    public Command(
            CommandType type) {
        this.type = type;
    }

    protected static byte[] getUtf8Bytes(String value) {
        try {
            return value.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }

    public abstract String toJson();

    @SuppressWarnings("unchecked")
    protected JSONObject toJsonObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(FIELD_TYPE, type.getIdentifier());
        return jsonObject;
    }

    protected static CommandType loadType(JSONObject jsonObject) {
        return CommandType.parseString((String) jsonObject
                .get(Command.FIELD_TYPE));
    }

    public static Command parseString(String json) {
        JSONObject jsonObject;
        try {
            jsonObject = (JSONObject) PARSER.parse(json);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

        CommandType type = loadType(jsonObject);
        if (type == null) {
            return null;
        }

        try {
            Method parser =
                    type.getType().getMethod("parseJson", JSONObject.class);
            return (Command) parser.invoke(null, jsonObject);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
