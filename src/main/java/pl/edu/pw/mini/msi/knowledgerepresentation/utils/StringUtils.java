package pl.edu.pw.mini.msi.knowledgerepresentation.utils;

/**
 * Created by Tomek on 2015-08-31.
 */
public class StringUtils {
    public static String concatenateStringAndBoolean(String s, boolean b) {
        String result = "";

        if (b == false) {
            result = result + "-";
        }
        result = result + s;

        return result;
    }

    public static String booleanTypicallyToString(boolean typically) {
        if (typically) {
            return "typically ";
        }
        else {
            return "";
        }
    }

    /**
     * To String Including Null
     * @param argument String, can be null.
     * @return String
     */
    public static String TSIN(Object argument) {
        if (argument == null) {
            return "NULL";
        }
        else {
            return argument.toString();
        }
    }

    public static byte countZerosAndOnes(String argument) {
        byte counter = 0;
        for (byte index = (byte)0; index < argument.length(); index++) {
            char argumentAtIndex = argument.charAt(index);
            if (argumentAtIndex == '0' || argumentAtIndex == '1') {
                counter++;
            }
        }
        return counter;
    }
}
