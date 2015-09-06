package pl.edu.pw.mini.msi.knowledgerepresentation.hoents;

import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts.Action;
import pl.edu.pw.mini.msi.knowledgerepresentation.utils.ArrayListOfByteUtils;
import pl.edu.pw.mini.msi.knowledgerepresentation.utils.HashMapByteStringUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Tomek on 2015-08-31.
 */
public class Hoent {
    public ArrayList<String> sysElemH;
    public ArrayList<HashMap<Byte, String>> sysElemO; //timeID actionID fluents
    public ArrayList<SysElemEAtTimeUnit> sysElemE;
    public ArrayList<SysElemNAtTimeUnit> sysElemN;
    public short tMax;
    public short fluentsCount;

    private Hoent(short tMax, short fluentsCount, boolean privateConstructor) {
        sysElemH = new ArrayList<String>(tMax);
        sysElemO = new ArrayList<HashMap<Byte, String>>(tMax);
        sysElemE = new ArrayList<SysElemEAtTimeUnit>(tMax);
        sysElemN = new ArrayList<SysElemNAtTimeUnit>(tMax);
        this.tMax = tMax;
        this.fluentsCount = fluentsCount;
    }

    public Hoent(short tMax, short fluentsCount) {
        sysElemH = new ArrayList<String>(tMax);
        sysElemO = new ArrayList<HashMap<Byte, String>>(tMax);
        sysElemE = new ArrayList<SysElemEAtTimeUnit>(tMax);
        sysElemN = new ArrayList<SysElemNAtTimeUnit>(tMax);
        this.tMax = tMax;
        this.fluentsCount = fluentsCount;

        StringBuilder fluentValuesQuestionMark = new StringBuilder("");
        for (short fluentIndex = 0; fluentIndex < fluentsCount; fluentIndex++) {
            fluentValuesQuestionMark = fluentValuesQuestionMark.append("?");
        }
        //StringBuilder fluentValuesZero = new StringBuilder("");
        //for (short fluentIndex = 0; fluentIndex < fluentsCount; fluentIndex++) {
        //    fluentValuesZero = fluentValuesZero.append("?");
        //}

        for (short timeIndex = 0; timeIndex < tMax; timeIndex++) {
            sysElemH.add( fluentValuesQuestionMark.toString() );
            sysElemO.add( new HashMap<Byte, String>() ); //fluentValuesZero.toString()
            sysElemE.add( new SysElemEAtTimeUnit() );
            sysElemN.add( new SysElemNAtTimeUnit() );
        }
    }

    public boolean hCheckCompatibility(String evaluationToTest, byte time) {
        if (time >= tMax) {
            return false;
        }

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
        if (time >= tMax) {
            StringBuilder resultSB = new StringBuilder("");
            for (byte fluentIndex = 0; fluentIndex < fluentsCount; fluentIndex++) {
                resultSB.append("-");
            }
            return resultSB.toString();
        }

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
            HashMap<Byte, String> sysElemOAtTimeUnit = HashMapByteStringUtils.copy(this.sysElemO.get(timeIndex));

            newHoent.sysElemH.add(new String(this.sysElemH.get(timeIndex)));
            newHoent.sysElemO.add( sysElemOAtTimeUnit );
            newHoent.sysElemE.add( this.sysElemE.get(timeIndex).copy() );
            newHoent.sysElemN.add( this.sysElemN.get(timeIndex).copy() );
        }

        return newHoent;
    }

    public void hAddNewEvaluates(String newEvaluates, byte time) {
        if (time >= tMax) {
            return;
        }

        //Hoent newHoent = this.copy();
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
        this.sysElemH.remove(time);
        this.sysElemH.add(time, newEvaluationInH.toString());

       // return this;
    }

    public boolean eIsActionAtTime(byte actionID, byte timeID) {
        if (timeID >= tMax) {
            return false;
        }

        return (this.sysElemE.get(timeID).occuringAction == actionID);
    }

    //    public ArrayList<HashMap<Byte, String>> sysElemO;
    public void oAddFluents(byte actionID, ArrayList<Byte> fluentsIDs, byte timeID) {
        if (timeID >= tMax) {
            return;
        }

        HashMap<Byte, String> oldSysElemO = this.sysElemO.get(timeID);

        if (oldSysElemO.containsKey(actionID)) {
            String oldSysElemOString = oldSysElemO.get(actionID);
            StringBuilder resultSB = new StringBuilder("");
            for (byte index = 0; index < fluentsCount; index++) {
                if (ArrayListOfByteUtils.contains(fluentsIDs, index)) {
                    resultSB.append("1");
                }
                else {
                    resultSB.append(oldSysElemOString.charAt(index));
                }
            }
            oldSysElemO.replace(actionID, resultSB.toString());
        }
        else {
            StringBuilder resultSB = new StringBuilder("");
            for (byte index = 0; index < fluentsCount; index++) {
                if (ArrayListOfByteUtils.contains(fluentsIDs, index)) {
                    resultSB.append("1");
                }
                else {
                    resultSB.append("0");
                }
            }
            oldSysElemO.put(actionID, resultSB.toString());
        }
    }

    public boolean eCanInsertActionAtTime(byte actionID, byte timeID) {
        if (timeID >= tMax) {
            return false; //20150905
        }
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
        if (timeID >= tMax) {
            return;
        }

        this.sysElemE.get(timeID).occuringAction = actionID;
    }

    public void eAddNegatedActionAtTime(byte actionID, byte timeID) {
        if (timeID >= tMax) {
            return;
        }

        ArrayList<Byte> disallowedActionsAL = this.sysElemE.get(timeID).disallowedActions;
        if (ArrayListOfByteUtils.contains(disallowedActionsAL, actionID) ) {
            return;
        }
        else {
            ArrayListOfByteUtils.insertIntoArrayList(disallowedActionsAL, actionID);
        }
    }

    public void nSetToTrue(byte timeID, byte actionID) {
        if (timeID  >= tMax) {
            return; //20150905
        }
        this.sysElemN.get(timeID).actionID = actionID;
        //this.sysElemN.add(timeID, new SysElemNAtTimeUnit(actionID));
    }

    public boolean hAreSysElemHsTheSame(ArrayList<String> otherSysElemH) {
        for (byte index = 0; index < this.sysElemH.size(); index++) {
            if (this.sysElemH.get(index).equals(otherSysElemH.get(index)) == false) {
                return false;
            }
            else {
                ;//empty
            }
        }
        return true;
    }

    public boolean nAreSysElemNsTheSame(ArrayList<SysElemNAtTimeUnit> otherSysElemN) {
        for (byte index = 0; index < this.sysElemN.size(); index++) {
            if (this.sysElemN.get(index).areSame(otherSysElemN.get(index)) == false) {
                return false;
            }
            else {
                ;//empty
            }
        }
        return true;
    }

    /**
     *
     * @param otherO
     * @return 0 - equals, 1 - is strictly in, 2 - other cases
     */
    public boolean oIsIn(ArrayList<HashMap<Byte, String>> otherO) {
        boolean equals = true;
        ArrayList<HashMap<Byte, String>> thisO = this.sysElemO;
        for (byte timeIndex = 0; timeIndex < thisO.size(); timeIndex++) {
            for (Byte b : thisO.get(timeIndex).keySet()) {
                if (otherO.get(timeIndex).containsKey(b) == false) {
                    return false; //2
                }
                String thisFluents = thisO.get(timeIndex).get(b);
                String otherFluents = otherO.get(timeIndex).get(b);
                byte result = Fluents.fluentsIsIn(thisFluents, otherFluents);
                if (result == 0) {
                    ; //empty
                }
                else if (result == 1) {
                    equals = false;
                }
                else if (result == 2) {
                    return false; //2 -
                }
            }
        }
        if (equals) {
            return false;
        }
        else {
            return true;
        }
    }

    public void hPreserveFluentsAtTime(byte newTimeIndex) {
        String oldTimeFluents = this.sysElemH.get(newTimeIndex - 1);
        String newTimeFluents = this.sysElemH.get(newTimeIndex);

        StringBuilder resultSB = new StringBuilder("");
        for (int fluentIndex = 0; fluentIndex < newTimeFluents.length(); fluentIndex++) {
            if (newTimeFluents.charAt(fluentIndex) == '?') {
                resultSB.append( oldTimeFluents.charAt(fluentIndex) );
            }
            else {
                resultSB.append( newTimeFluents.charAt(fluentIndex) );
            }
        }
        String result = resultSB.toString();
        this.sysElemH.remove(newTimeIndex);
        this.sysElemH.add(newTimeIndex, result);
    }

    public boolean nIsIn(ArrayList<SysElemNAtTimeUnit> otherN) {
        boolean equals = true;
        ArrayList<SysElemNAtTimeUnit> thisN = this.sysElemN;
        for (byte timeIndex = 0; timeIndex < thisN.size(); timeIndex++) {
            if (thisN.get(timeIndex).actionID == -1) {
                if (otherN.get(timeIndex).actionID == -1) {
                    ; //empty (equal)
                }
                else {
                    equals = false;
                }
            }
            else {
                if (thisN.get(timeIndex).actionID == otherN.get(timeIndex).actionID) {
                    ; //empty (equals)
                }
                else {
                    return false; //different
                }
            }
        }
        if (equals) {
            return false;
        }
        else {
            return true;
        }
    }

    public boolean eIsAgentInvolvedAtTime(String agentToCheck, byte timeID, ArrayList<String> actionsMapping) {
        SysElemEAtTimeUnit e = this.sysElemE.get(timeID);
        byte actionID = e.occuringAction;
        String actionString = actionsMapping.get(actionID);
        boolean result = Action.isAgentInActionString(actionString, agentToCheck);
        return result;
    }

    public boolean eIsIn(ArrayList<SysElemEAtTimeUnit> otherE) {
        boolean equals = true;
        ArrayList<SysElemEAtTimeUnit> thisE = this.sysElemE;
        for (byte timeIndex = 0; timeIndex < thisE.size(); timeIndex++) {
            if (thisE.get(timeIndex).occuringAction == -1) {
                if (otherE.get(timeIndex).occuringAction == -1) {
                    ; //empty (equal)
                }
                else {
                    equals = false;
                }
            }
            else {
                if (thisE.get(timeIndex).occuringAction == otherE.get(timeIndex).occuringAction) {
                    ; //empty (equals)
                }
                else {
                    return false; //different
                }
            }
        }
        if (equals) {
            return false;
        }
        else {
            return true;
        }
    }

    //ArrayList<HashMap<Byte, String>> sysElemO; //timeID actionID fluents
    public boolean oAreSysElemOsTheSame(ArrayList<HashMap<Byte, String>> otherSysElemO) {
        for (byte index = 0; index < this.sysElemN.size(); index++) {
            if (this.sysElemO.get(index).keySet().size() == 0 &&
                    otherSysElemO.get(index).keySet().size() == 0) {
                ;//do nothing
            }
            else if (this.sysElemO.get(index).keySet().size() == 0 ||
                    otherSysElemO.get(index).keySet().size() == 0) {
                return false;
            }
            //compare actionID
            else if (this.sysElemO.get(index).keySet().iterator().next() == otherSysElemO.get(index).keySet().iterator().next()) {
                byte thisActionIDAtTime = this.sysElemO.get(index).keySet().iterator().next();
                if (this.sysElemO.get(index).get( thisActionIDAtTime ).equals(
                        otherSysElemO.get(index).get( thisActionIDAtTime ))) {
                    ;//do nothing
                }
                else {
                    return false;
                }

            }
            else {
                return false;
            }
        }
        return true;
    }

    public int nCountNs() {
        int result = 0;
        for (byte index = 0; index < tMax; index++) {
            SysElemNAtTimeUnit sysElemN = this.sysElemN.get(index);
            if (sysElemN.actionID != -1) {
                result++;
            }
        }
        return result;
    }
}
