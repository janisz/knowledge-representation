package pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.pw.mini.msi.knowledgerepresentation.utils.ArrayListOfStringUtils;

import java.util.ArrayList;

/**
 * Created by Tomek on 2015-08-29.
 */
public class ConjunctionFormula implements IFormula{

    private static final Logger log = LoggerFactory.getLogger(ConjunctionFormula.class);

    public ArrayList<Boolean> negations = new ArrayList<Boolean>();
    public ArrayList<Byte> fluentIDs = new ArrayList<Byte>();
    public ArrayList<String> fluents = new ArrayList<String>();

    public void addFluent(boolean negated, String fluent){
        this.negations.add(negated);
        this.fluents.add(fluent);
    }

    @Override
    public char evaluateForValues(String evaluatee) {
        for (int index = 0; index < fluentIDs.size(); index++) {
            char charAt = evaluatee.charAt(fluentIDs.get(index));
            short shortAt = FormulaUtils.getShortForChar(charAt);
            if (negations.get(index)) {
                shortAt = (short)(1 - shortAt); //0->1; 1->0
            }
            if (shortAt == 0) {
                return '0';
            }
        }
        return '1';
    }

    @Override
    public ArrayList<String> getFluents() {
        return fluents;
    }

    @Override
    public ArrayList<Byte> getFluentsIDs() {
        return fluentIDs;
    }

    @Override
    public String getFluentsMask(short fluentCount) {
        StringBuilder resultSB = new StringBuilder(fluentCount);
        for (short index = 0; index < fluentCount; index++) {
            resultSB.append("-");
        }
        for (short fluentID : fluentIDs) {
            resultSB.replace(fluentID, fluentID + 1, "+");
        }
        return resultSB.toString();
    }

    @Override
    public void fillFluentsIDs(ArrayList<String> fluentsMapping) {
        for (String fluent : fluents) {
            byte fluentID = ArrayListOfStringUtils.getIndexOfString(fluentsMapping, fluent);
            fluentIDs.add(fluentID);
        }
    }

    @Override
    public String toString() {
        String result = ArrayListOfStringUtils.myToStringDouble(fluents, negations);
        return result;
    }

    /*@Override
    public char evaluateForValues(String evaluatee) {
        char charAt = evaluatee.charAt(fluentID);
        if (charAt == '?') {
            return '?';
        }
        else if (charAt == '0') {
            if (negated) {
                return '1';
            }
            else {
                return '0';
            }
        }
        else if (charAt == '1') {
            if (negated) {
                return '0';
            }
            else {
                return '1';
            }
        }
        return 'e'; //error
    }*/

}
