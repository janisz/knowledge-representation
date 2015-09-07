package pl.edu.pw.mini.msi.knowledgerepresentation.hoents;

import pl.edu.pw.mini.msi.knowledgerepresentation.utils.ArrayListOfStringUtils;

import java.util.ArrayList;

/**
 * TODO TOMEKL
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
                if (secondCharAt != '0' && secondCharAt == '1') {
                    isStrictlyIn = true;
                }
            } else if (firstCharAt == '1') {
                if (secondCharAt != '1' && secondCharAt == '0') {
                    return 2;
                }
            }
        }
        if (!isStrictlyIn) {
            return 0;
        } else {
            return 1;
        }
    }

    public static ArrayList<String> expandQuestionMarks(String argument) {
        ArrayList<String> results = new ArrayList<>();
        results.add("");

        for (byte index = 0; index < argument.length(); index++) {
            ArrayList<String> newResults = new ArrayList<>();
            for (String oldResult : results) {
                if (argument.charAt(index) == '?') {
                    newResults.add(oldResult + "0");
                    newResults.add(oldResult + "1");
                } else {
                    newResults.add(oldResult + argument.charAt(index));
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
        results.add("");

        for (byte index = 0; index < oldFluents.length(); index++) {
            ArrayList<String> newResults = new ArrayList<String>();
            for (String oldResult : results) {
                //======================================================================================================
                if (newFluents.charAt(index) == '?') {
                    if (changingFluents.charAt(index) == '1') {
                        newResults.add(oldResult + "0");
                        newResults.add(oldResult + "1");
                    } else {
                        newResults.add(oldResult + oldFluents.charAt(index));
                    }
                }
                //======================================================================================================
                else {
                    newResults.add(oldResult + newFluents.charAt(index));
                }
            }
            results = newResults;
        }

        return results;
    }

    public static boolean checkCompatibilityUsingMask(String oldFluents, String newFluents, String changingFluents,
                                                      ArrayList<String> posEvalsFromAtSentences) {

        for (byte index = 0; index < oldFluents.length(); index++) {
            if ((newFluents.charAt(index) == '0' || newFluents.charAt(index) == '1')
                    && (oldFluents.charAt(index) == '0' || oldFluents.charAt(index) == '1')
                    && newFluents.charAt(index) != oldFluents.charAt(index)
                    && (posEvalsFromAtSentences == null || posEvalsFromAtSentences.size() <= 0
                            || !isNewValueisInOneOfChangedFluents(newFluents.charAt(index), index, posEvalsFromAtSentences))
                    && (changingFluents == null || changingFluents.charAt(index) == '0')) {
                return false;
            }
        }

        return true;
    }

    public static ArrayList<String> getFluentsConjunction(ArrayList<String> posEvaluates,
                                                          ArrayList<String> posEvaluatesForSentence) {
        ArrayList<String> results = new ArrayList<String>();
        for (String posEvaluate : posEvaluates) {
            for (String posEvaluateForSentence : posEvaluatesForSentence) {
                if (!hCheckCompatibility(posEvaluate, posEvaluateForSentence, (byte) posEvaluate.length())) {
                    continue;
                }
                String newEvaluates = hGetNewEvaluates(posEvaluate, posEvaluateForSentence);
                newEvaluates = hAddNewEvaluates(posEvaluate, newEvaluates);
                if (!ArrayListOfStringUtils.contains(results, newEvaluates)) {
                    results.add(newEvaluates);
                }
            }
        }
        return results;
    }

    public static boolean hCheckCompatibility(String firstEvaluation, String secondEvaluation, byte fluentsCount) {
        for (byte fluentIndex = 0; fluentIndex < fluentsCount; fluentIndex++) {
            char charAtIndexInActEval = firstEvaluation.charAt(fluentIndex);
            char charAtIndexInEvalToTest = secondEvaluation.charAt(fluentIndex);

            if (charAtIndexInEvalToTest != '?') {
                if (charAtIndexInEvalToTest == '1') {
                    if (charAtIndexInActEval != '1' && charAtIndexInActEval == '0') {
                        return false;
                    }
                } else if (charAtIndexInEvalToTest == '0') {
                    if (charAtIndexInActEval != '0' && charAtIndexInActEval == '1') {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static String hGetNewEvaluates(String firstEvaluation, String secondEvaluation) {
        byte fluentsCount = (byte) firstEvaluation.length();

        StringBuilder resultSB = new StringBuilder("");
        duplicatedCode2(fluentsCount, resultSB, firstEvaluation, secondEvaluation);
        return resultSB.toString();
    }

    public static void duplicatedCode2(byte fluentsCount, StringBuilder resultSB, String evaluationInH, String posEvaluate) {
        for (byte fluentIndex = 0; fluentIndex < fluentsCount; fluentIndex++) {
            char charAtIndexInEvalInH = evaluationInH.charAt(fluentIndex);
            char charAtIndexInEvalToTest = posEvaluate.charAt(fluentIndex);
            if ((charAtIndexInEvalToTest == '0' || charAtIndexInEvalToTest == '1')
                    && (charAtIndexInEvalInH == '?')) {
                resultSB.append(charAtIndexInEvalToTest);
            } else {
                resultSB.append("-");
            }
        }
    }


    public static String hAddNewEvaluates(String firstEvaluation, String secondEvaluation) {
        byte fluentsCount = (byte) firstEvaluation.length();
        StringBuilder newEvaluationInH = new StringBuilder("");
        Hoent.duplicatedCode(secondEvaluation, fluentsCount, firstEvaluation, newEvaluationInH);
        return newEvaluationInH.toString();
    }

    public static boolean isNewValueisInOneOfChangedFluents(char newValue, byte fluentIndex,
                                                            ArrayList<String> posEvaluationsFromAtSentences) {
        if (posEvaluationsFromAtSentences == null || posEvaluationsFromAtSentences.size() == 0) {
            return true;
        }
        for (String posEvaluationFromAtSentences : posEvaluationsFromAtSentences) {
            if (newValue == posEvaluationFromAtSentences.charAt(fluentIndex)) {
                return true;
            }
        }
        return false;
    }
}
