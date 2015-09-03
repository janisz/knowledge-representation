package pl.edu.pw.mini.msi.knowledgerepresentation.hoents;

import java.util.ArrayList;

/** TODO TOMEKL
 * Created by Tomek on 2015-09-03.
 */
public class Fluents {

    /**
     * @param first
     * @param second
     * @return 0 - equals, 1 - first is strictly in second, 2 - other cases
     */
    public static byte fluentsIsIn(String first, String second) {
        boolean isStrictlyIn = false;
        for (byte index = 0; index < first.length(); index++) {
            char firstCharAt = first.charAt(index);
            char secondCharAt = second.charAt(index);
            if (firstCharAt == '0') {
                if (secondCharAt == '0') {
                    ;
                }
                else if (secondCharAt == '1') {
                    isStrictlyIn = true;
                }
            }
            else if (firstCharAt == '1') {
                if (secondCharAt == '1') {
                    ;
                }
                else if (secondCharAt == '0') {
                    return 2;
                }
            }
        }
        if (isStrictlyIn == false) {
            return 0;
        }
        else {
            return 1;
        }
    }

    public static ArrayList<String> expandQuestionMarks(String argument) {
        ArrayList<String> results = new ArrayList<String>();
        results.add( new String("") );

        for (byte index = 0; index < argument.length(); index++) {
            ArrayList<String> newResults = new ArrayList<String>();
            for (String oldResult : results) {
                if (argument.charAt(index) == '?') {
                    newResults.add( new String(oldResult + "0") );
                    newResults.add( new String(oldResult + "1") );
                }
                else {
                    newResults.add( new String(oldResult + argument.charAt(index)) );
                }
            }
            results = newResults;
        }

        return results;
    }

    public static byte countQuestionMarks(String argument) {
        byte counter = 0;

        for (byte index = 0; index < argument.length(); index++) {
            if (argument.charAt(index) == '?') {
                counter++;
            }
        }

        return counter;
    }

    public static ArrayList<String> expandQuestionMarksWithMask(String oldFluents, String newFluents, String changingFluents) {
        ArrayList<String> results = new ArrayList<String>();
        results.add( new String("") );

        for (byte index = 0; index < oldFluents.length(); index++) {
            ArrayList<String> newResults = new ArrayList<String>();
            for (String oldResult : results) {
                //======================================================================================================
                if (newFluents.charAt(index) == '?') {
                    if (changingFluents.charAt(index) == '1') {
                        newResults.add(new String(oldResult + "0"));
                        newResults.add(new String(oldResult + "1"));
                    }
                    else {
                        newResults.add(new String(oldResult + oldFluents.charAt(index)));
                    }
                }
                //======================================================================================================
                else {
                    newResults.add( new String(oldResult + newFluents.charAt(index)) );
                }
            }
            results = newResults;
        }

        return results;
    }
}
