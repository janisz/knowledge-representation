package pl.edu.pw.mini.msi.knowledgerepresentation.utils;

import java.util.ArrayList;

/**
 * Created by Tomek on 2015-08-31.
 */
public class ArrayListOfStringUtils {
    public static byte getIndexOfString(ArrayList<String> list, String string) {
        for (String listElem : list) {
            if (listElem.compareTo(string) == 0) {
                return (byte)list.indexOf(listElem);
            }
        }
        return (short)-1;
    }

    public static String myToString(ArrayList<String> list) {
        StringBuilder resultSB = new StringBuilder();
        resultSB.append("[");
        for (int index = 0; index < list.size(); index++) {
            if (index == 0) {
                resultSB.append(list.get(index));
            }
            else {
                resultSB.append(",");
                resultSB.append(list.get(index));
            }
        }
        resultSB.append("]");

        return resultSB.toString();
    }

    public static String myToStringDouble(ArrayList<String> listS, ArrayList<Boolean> listB) {
        StringBuilder resultSB = new StringBuilder();
        resultSB.append("[");
        for (int index = 0; index < listS.size(); index++) {
            if (index == 0) {
                String s = StringUtils.concatenateStringAndBoolean(listS.get(index), listB.get(index));
                resultSB.append(s);
            }
            else {
                resultSB.append(",");
                String s = StringUtils.concatenateStringAndBoolean(listS.get(index), listB.get(index));
                resultSB.append(s);
            }
        }
        resultSB.append("]");

        return resultSB.toString();
    }
}
