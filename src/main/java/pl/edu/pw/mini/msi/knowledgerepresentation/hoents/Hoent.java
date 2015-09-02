package pl.edu.pw.mini.msi.knowledgerepresentation.hoents;

import pl.edu.pw.mini.msi.knowledgerepresentation.utils.ArrayListOfByteUtils;

import java.util.ArrayList;

/**
 * Created by Tomek on 2015-08-31.
 */
public class Hoent {
    public ArrayList<String> sysElemH;
    public ArrayList<String> sysElemO;
    public ArrayList<SysElemEAtTimeUnit> sysElemE;
    public ArrayList<Boolean> sysElemN;
    public short tMax;
    public short fluentsCount;

    private Hoent(short tMax, short fluentsCount, boolean privateConstructor) {
        sysElemH = new ArrayList<String>(tMax);
        sysElemO = new ArrayList<String>(tMax);
        sysElemE = new ArrayList<SysElemEAtTimeUnit>(tMax);
        sysElemN = new ArrayList<Boolean>(tMax);
        this.tMax = tMax;
        this.fluentsCount = fluentsCount;
    }

    public Hoent(short tMax, short fluentsCount) {
        sysElemH = new ArrayList<String>(tMax);
        sysElemO = new ArrayList<String>(tMax);
        sysElemE = new ArrayList<SysElemEAtTimeUnit>(tMax);
        sysElemN = new ArrayList<Boolean>(tMax);
        this.tMax = tMax;
        this.fluentsCount = fluentsCount;

        StringBuilder fluentValuesQuestionMark = new StringBuilder("");
        for (short fluentIndex = 0; fluentIndex < fluentsCount; fluentIndex++) {
            fluentValuesQuestionMark = fluentValuesQuestionMark.append("?");
        }
        StringBuilder fluentValuesZero = new StringBuilder("");
        for (short fluentIndex = 0; fluentIndex < fluentsCount; fluentIndex++) {
            fluentValuesZero = fluentValuesZero.append("?");
        }

        for (short timeIndex = 0; timeIndex < tMax; timeIndex++) {
            sysElemH.add( fluentValuesQuestionMark.toString() );
            sysElemO.add( fluentValuesZero.toString() );
            sysElemE.add( new SysElemEAtTimeUnit() );
            sysElemN.add( false );
        }
    }

    public boolean hCheckCompatibility(String evaluationToTest, byte time) {
        String actualEvaluation = sysElemH.get(time);
        for (byte fluentIndex = 0; fluentIndex < fluentsCount; fluentIndex++) {
            char charAtIndexInActEval = actualEvaluation.charAt(fluentIndex);
            char charAtIndexInEvalToTest = evaluationToTest.charAt(fluentIndex);

            if (charAtIndexInEvalToTest == '?') {
                continue;
            }
            else if (charAtIndexInEvalToTest == '1') {
                if (charAtIndexInActEval == '1') {
                    continue;
                }
                else if (charAtIndexInActEval == '0') {
                    return false;
                }
                else if (charAtIndexInActEval == '?') {
                    continue;
                }
            }
            else if (charAtIndexInEvalToTest == '0') {
                if (charAtIndexInActEval == '0') {
                    continue;
                }
                else if (charAtIndexInActEval == '1') {
                    return false;
                }
                else if (charAtIndexInActEval == '?') {
                    continue;
                }
            }
        }
        return true;
    }

    public String hGetNewEvaluates(String posEvaluate, byte time) {
        StringBuilder resultSB = new StringBuilder("");
        String evaluationInH = sysElemH.get(time);
        for (byte fluentIndex = 0; fluentIndex < fluentsCount; fluentIndex++) {
            char charAtIndexInEvalInH = evaluationInH.charAt(fluentIndex);
            char charAtIndexInEvalToTest = posEvaluate.charAt(fluentIndex);
            if ( (charAtIndexInEvalToTest == '0' || charAtIndexInEvalToTest == '1')
                    && (charAtIndexInEvalInH == '?') ){
                resultSB.append(charAtIndexInEvalToTest);
            }
            else {
                resultSB.append("-");
            }
        }
        return resultSB.toString();
    }

    public Hoent copy() {
        Hoent newHoent = new Hoent(tMax, fluentsCount, true);

        for (short timeIndex = 0; timeIndex < tMax; timeIndex++) {
            sysElemH.add( new String( this.sysElemH.get(timeIndex) ) );
            sysElemO.add( new String( this.sysElemO.get(timeIndex) ) );
            sysElemE.add( this.sysElemE.get(timeIndex).copy() );
            sysElemN.add( false );
        }

        return newHoent;
    }

    public Hoent hAddNewEvaluates(String newEvaluates, byte time) {
        Hoent newHoent = this.copy();
        String evaluationInH = sysElemH.get(time);
        StringBuilder newEvaluationInH = new StringBuilder("");
        for (byte fluentIndex = 0; fluentIndex < fluentsCount; fluentIndex++) {
            char charAtIndexInEvalInH = evaluationInH.charAt(fluentIndex);
            char charAtIndexInNewEvaluates = newEvaluates.charAt(fluentIndex);
            if (charAtIndexInNewEvaluates == '0' || charAtIndexInNewEvaluates == '1') {
                newEvaluationInH.append(charAtIndexInNewEvaluates);
            }
            else {
                newEvaluationInH.append(charAtIndexInEvalInH);
            }
        }
        newHoent.sysElemH.remove(time);
        newHoent.sysElemH.add(time, newEvaluationInH.toString());

        return newHoent;
    }

    public boolean eIsActionAtTime(byte actionID, byte timeID) {
        return (this.sysElemE.get(timeID).occuringAction == actionID);
    }

    public void oAddFluents(ArrayList<Byte> fluentsIDs, byte timeID) {
        String oldSysElemO = this.sysElemO.get(timeID);
        StringBuilder resultSB = new StringBuilder("");
        for (byte index = 0; index < fluentsCount; index++) {
            if (ArrayListOfByteUtils.contains(fluentsIDs, index)) {
                resultSB.append("1");
            }
            else {
                resultSB.append(oldSysElemO.charAt(index));
            }
        }
        this.sysElemO.remove(timeID);
        this.sysElemO.add(timeID, resultSB.toString());
    }

    public boolean eCanInsertActionAtTime(byte actionID, byte timeID) {
        if (this.sysElemE.get(timeID).occuringAction == actionID) {
            return true;
        }
        else if (ArrayListOfByteUtils.contains(this.sysElemE.get(timeID).disallowedActions, actionID) == true) {
            return false;
        }
        else if (this.sysElemE.get(timeID).occuringAction > -1 && this.sysElemE.get(timeID).occuringAction != actionID) {
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * Before adding check if it is possible with eCanInsertActionAtTime(...).
     * @param actionID
     * @param timeID
     */
    public void eAddAction(byte actionID, byte timeID) {
        this.sysElemE.get(timeID).occuringAction = actionID;
    }

    public void eAddNegatedActionAtTime(byte actionID, byte timeID) {
        ArrayList<Byte> disallowedActionsAL = this.sysElemE.get(timeID).disallowedActions;
        if (ArrayListOfByteUtils.contains(disallowedActionsAL, actionID) ) {
            return;
        }
        else {
            ArrayListOfByteUtils.insertIntoArrayList(disallowedActionsAL, actionID);
        }
    }

    public void nSetToTrue(byte timeID) {
        this.sysElemN.remove(timeID);
        this.sysElemN.add(timeID, new Boolean(true));
    }
}
