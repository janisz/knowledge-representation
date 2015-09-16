package pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Created by Tomek on 2015-08-30.
 */
public class FormulaUtils {

    private static final Logger log = LoggerFactory.getLogger(FormulaUtils.class);

    public static ArrayList<ArrayList<String>> getPositiveAndNegativeEvaluates(IFormula formula, short fluentsCount) {

        //String mask = null;
        //if (formula == null) {
        //mask = FormulaUtils.getFullFluentsMask(fluentsCount);
        //}
        //else {
        String mask = formula.getFluentsMask(fluentsCount);
        //}
        ArrayList<String> allEvaluates = getAllEvaluates(fluentsCount, mask);
        ArrayList<String> positiveEvaluates = new ArrayList<>();
        ArrayList<String> negativeEvaluates = new ArrayList<>();

        for (String evaluate : allEvaluates) {
            char formulaEvaluate = formula.evaluateForValues(evaluate);
            if (formulaEvaluate == '1') {
                positiveEvaluates.add(evaluate);
            } else if (formulaEvaluate == '0') {
                negativeEvaluates.add(evaluate);
            }
        }

        ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
        result.add(positiveEvaluates);
        result.add(negativeEvaluates);

        return result;
    }

    /* old version
    public static ArrayList<ArrayList<String>> getPositiveAndNegativeEvaluates(IFormula formula, short fluentCount) {

        ArrayList<String> allEvaluates = getAllEvaluates(fluentCount);
        ArrayList<String> positiveEvaluates = new ArrayList<>();
        ArrayList<String> negativeEvaluates = new ArrayList<>();

        for (String evaluate : allEvaluates) {
            char formulaEvaluate = formula.evaluateForValues(evaluate);
            if (formulaEvaluate == '1') {
                positiveEvaluates.add(evaluate);
            }
            else if (formulaEvaluate == '0') {
                negativeEvaluates.add(evaluate);
            }
        }

        ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
        result.add(positiveEvaluates);
        result.add(negativeEvaluates);

        return result;
    }*/

    public static ArrayList<String> restrictEvaluates(ArrayList<String> evaluates, String restricting) {

        ArrayList<String> results = new ArrayList<>();

        outerLoop:
        for (String restricted : evaluates) {
            if (restricted.length() != restricting.length()) {
                log.debug("restricted.length() != restricting.length()");
                return null;
            }
            for (int index = 0; index < restricted.length(); index++) {
                if ((restricting.charAt(index) != '1' || restricted.charAt(index) != '1')
                        && (restricting.charAt(index) != '0' || restricted.charAt(index) != '0')
                        && restricting.charAt(index) != '?') {
                    continue outerLoop;
                }
            }
            results.add(restricted);
        }

        return results;
    }

    /**
     * @param ch We assume, that it was checked before for being '?'
     * @return
     */
    public static short getShortForChar(char ch) {
        if (ch == '0') {
            return 0;
        } else if (ch == '1') {
            return 1;
        } else {
            return -1;
        }
    }
    //===================================================================================================

    private static ArrayList<String> getAllEvaluates(short fluentCount, String mask) {
        short processedLength = 0;
        ArrayList<String> results = new ArrayList<String>();
        String emptyString = "";
        results.add(emptyString);

        while (processedLength < fluentCount) {
            ArrayList<String> newResults = new ArrayList<String>();
            for (String result : results) {
                if (mask.charAt(processedLength) == '+') {
                    String newResultWithZero = result + '0';
                    String newResultWithOne = result + '1';
                    newResults.add(newResultWithZero);
                    newResults.add(newResultWithOne);
                } else {
                    String newResultWithQuestionMark = result + '?';
                    newResults.add(newResultWithQuestionMark);
                }
            }
            results = newResults;
            processedLength++;
        }

        return results;
    }

}
