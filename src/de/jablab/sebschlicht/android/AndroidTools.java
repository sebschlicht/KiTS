package de.jablab.sebschlicht.android;

import java.util.Locale;

public class AndroidTools {

    /**
     * converts a file name to a valid file name for an Android resource
     * <ul>
     * <li>using lower case characters</li>
     * <li>replacing spaces to "_"</li>
     * <li>replacing umlauts</li>
     * <li>removing characters not according to identifier specifications of
     * Java</li>
     * </ul>
     * 
     * @param fileName
     *            any file name to be converted
     * @return file name valid for Android resources
     * 
     * @see http://docs.oracle.com/javase/specs/jls/se5.0/html/lexical.html#3.8
     */
    public static String toValidResourceName(String fileName) {
        StringBuilder name = new StringBuilder();
        if (!Character.isJavaIdentifierStart(fileName.charAt(0))) {
            name.append("_");
        }
        for (char c : fileName.toLowerCase(Locale.ENGLISH).toCharArray()) {
            // replace spaces with '_'
            if (c == ' ') {
                name.append('_');
            }
            // append letters replacing umlauts
            else if (Character.isLetter(c)) {
                name.append(convertUmlaut(c));
            }
            // append allowed characters as-is
            else if (Character.isJavaIdentifierPart(c)) {
                name.append(c);
            }
        }
        return name.toString();
    }

    /**
     * converts an umlaut to alternative ASCII characters,<br>
     * e.g. <b>Ü</b> to <b>Ue</b><br>
     * recognizes ß, ü/Ü, ö/Ö, ä/Ä
     * 
     * @param character
     *            any character that may be an umlaut
     * @return alternative characters for umlaut<br>
     *         or original character if no umlaut recognized
     */
    public static String convertUmlaut(char character) {
        if (character == 'ß') {
            return "ss";
        } else if (character == 'ü') {
            return "ue";
        } else if (character == 'Ü') {
            return "Ue";
        } else if (character == 'ö') {
            return "oe";
        } else if (character == 'Ö') {
            return "Oe";
        } else if (character == 'ä') {
            return "ae";
        } else if (character == 'Ä') {
            return "Ae";
        }
        return new String(new char[] {
            character
        });
    }
}
