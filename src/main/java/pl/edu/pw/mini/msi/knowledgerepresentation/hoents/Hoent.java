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
    public final ArrayList<String> sysElemH;
    public final ArrayList<HashMap<Byte, String>> sysElemO; //timeID actionID fluents
    public final ArrayList<SysElemEAtTimeUnit> sysElemE;
    public final ArrayList<SysElemNAtTimeUnit> sysElemN;
    public final ArrayList<SysElemAAtTimeUnit> sysElemA;
    public final short tMax;
    public final short fluentsCount;
    public boolean hasExceededTimeLimit;
    public boolean hasContradiction;
    public byte firstTypicalActionIndex;
    private byte errorInsertingActionAtTime;

    /**
     * Constructor for copy method
     *
     * @param tMax
     * @param fluentsCount
     */
    private Hoent(short tMax, short fluentsCount, boolean hasExceededTimeLimit, boolean hasContradiction,
                  byte firstTypicalActionIndex, byte errorInsertingActionAtTime) {
        sysElemH = new ArrayList<>(tMax);
        sysElemO = new ArrayList<>(tMax);
        sysElemE = new ArrayList<>(tMax);
        sysElemN = new ArrayList<>(tMax);
        sysElemA = new ArrayList<>(tMax);
        this.tMax = tMax;
        this.fluentsCount = fluentsCount;
        this.hasExceededTimeLimit = hasExceededTimeLimit;
        this.hasContradiction = hasContradiction;
        this.firstTypicalActionIndex = firstTypicalActionIndex;
        this.errorInsertingActionAtTime = errorInsertingActionAtTime;
    }

    public Hoent(short tMax, short fluentsCount) {
        sysElemH = new ArrayList<>(tMax);
        sysElemO = new ArrayList<>(tMax);
        sysElemE = new ArrayList<>(tMax);
        sysElemN = new ArrayList<>(tMax);
        sysElemA = new ArrayList<>(tMax);
        this.tMax = tMax;
        this.fluentsCount = fluentsCount;
        this.hasExceededTimeLimit = false;
        this.hasContradiction = false;
        this.firstTypicalActionIndex = -1;
        this.errorInsertingActionAtTime = -1;

        StringBuilder fluentValuesQuestionMark = new StringBuilder("");
        for (short fluentIndex = 0; fluentIndex < fluentsCount; fluentIndex++) {
            fluentValuesQuestionMark = fluentValuesQuestionMark.append("?");
        }

        for (short timeIndex = 0; timeIndex < tMax; timeIndex++) {
            sysElemH.add(fluentValuesQuestionMark.toString());
            sysElemO.add(new HashMap<>()); //fluentValuesZero.toString()
            sysElemE.add(new SysElemEAtTimeUnit());
            sysElemN.add(new SysElemNAtTimeUnit());
            sysElemA.add(new SysElemAAtTimeUnit());
        }
    }

    public boolean hCheckCompatibility(String evaluationToTest, byte time) {
        return time < tMax && Fluents.hCheckCompatibility(sysElemH.get(time), evaluationToTest, (byte) this.fluentsCount);

    }

    public String hGetNewEvaluates(String posEvaluate, byte time) {
        if (time >= tMax) {
            StringBuilder resultSB = new StringBuilder("");
            for (byte fluentIndex = 0; fluentIndex < fluentsCount; fluentIndex++) {
                resultSB.append("-");
            }
            return resultSB.toString();
        }

        return Fluents.hGetNewEvaluates(sysElemH.get(time), posEvaluate);
    }

    public Hoent copy() {
        Hoent newHoent = new Hoent(this.tMax, this.fluentsCount, this.hasExceededTimeLimit, this.hasContradiction,
                this.firstTypicalActionIndex, this.errorInsertingActionAtTime);

        for (short timeIndex = 0; timeIndex < tMax; timeIndex++) {
            HashMap<Byte, String> sysElemOAtTimeUnit = HashMapByteStringUtils.copy(this.sysElemO.get(timeIndex));

            newHoent.sysElemH.add(this.sysElemH.get(timeIndex));
            newHoent.sysElemO.add(sysElemOAtTimeUnit);
            newHoent.sysElemE.add(this.sysElemE.get(timeIndex).copy());
            newHoent.sysElemN.add(this.sysElemN.get(timeIndex).copy());
            newHoent.sysElemA.add(this.sysElemA.get(timeIndex).copy());
        }

        return newHoent;
    }

    public void hAddNewEvaluates(String newEvaluates, byte time) {
        if (time >= tMax) {
            return;
        }

        String newEvaluationInH = Fluents.hAddNewEvaluates(sysElemH.get(time), newEvaluates);

        this.sysElemH.remove(time);
        this.sysElemH.add(time, newEvaluationInH);
    }

    public boolean eIsActionAtTime(byte actionID, byte timeID) {
        return timeID < tMax && (this.sysElemE.get(timeID).occuringAction == actionID);

    }

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
                } else {
                    resultSB.append(oldSysElemOString.charAt(index));
                }
            }
            oldSysElemO.replace(actionID, resultSB.toString());
        } else {
            StringBuilder resultSB = new StringBuilder("");
            for (byte index = 0; index < fluentsCount; index++) {
                if (ArrayListOfByteUtils.contains(fluentsIDs, index)) {
                    resultSB.append("1");
                } else {
                    resultSB.append("0");
                }
            }
            oldSysElemO.put(actionID, resultSB.toString());
        }
    }

    public boolean eCanInsertActionAtTime(byte actionID, byte timeID) {
        //20150905
        return timeID < tMax
                && (this.sysElemE.get(timeID).occuringAction == actionID || !ArrayListOfByteUtils.contains(this.sysElemE.get(timeID).disallowedActions, actionID)
                && !(this.sysElemE.get(timeID).occuringAction > -1
                && this.sysElemE.get(timeID).occuringAction != actionID));
    }

    /**
     * Before adding check if it is possible with eCanInsertActionAtTime(...).
     *
     * @param actionID
     * @param timeID
     */
    public void eAddAction(byte actionID, byte timeID) {
        if (timeID >= tMax) {
            return;
        }
        if (this.sysElemE.get(timeID).occuringAction != -1) {
            return;
        }

        this.sysElemE.get(timeID).occuringAction = actionID;
    }

    public boolean eAddNegatedActionAtTime(byte actionID, byte timeID) {
        if (timeID >= tMax) {
            return false;
        }

        if (this.sysElemE.get(timeID).occuringAction == actionID) { //20150909
            return false;
        }

        ArrayList<Byte> disallowedActionsAL = this.sysElemE.get(timeID).disallowedActions;
        if (ArrayListOfByteUtils.contains(disallowedActionsAL, actionID)) {
            return true;
        } else {
            ArrayListOfByteUtils.insertIntoArrayList(disallowedActionsAL, actionID);
            return true;
        }
    }

    public boolean eCanAddNegatedActionAtTime(byte actionID, byte timeID) {
        if (timeID >= tMax) {
            return false;
        }

        return this.sysElemE.get(timeID).occuringAction != actionID;

    }

    public void nSetToTrue(byte timeID, byte actionID) {
        if (timeID >= tMax) {
            return; //20150905
        }
        this.sysElemN.get(timeID).actionID = actionID;
        //this.sysElemN.add(timeID, new SysElemNAtTimeUnit(actionID));
    }

    public boolean hAreSysElemHsTheSame(ArrayList<String> otherSysElemH) {
        for (byte index = 0; index < this.sysElemH.size(); index++) {
            if (!this.sysElemH.get(index).equals(otherSysElemH.get(index))) {
                return false;
            }
        }
        return true;
    }

    public boolean nAreSysElemNsTheSame(ArrayList<SysElemNAtTimeUnit> otherSysElemN) {
        for (byte index = 0; index < this.sysElemN.size(); index++) {
            if (!this.sysElemN.get(index).areSame(otherSysElemN.get(index))) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param otherO
     * @return 0 - equals, 1 - is strictly in, 2 - other cases
     */
    public boolean oIsIn(ArrayList<HashMap<Byte, String>> otherO) {
        boolean equals = true;
        ArrayList<HashMap<Byte, String>> thisO = this.sysElemO;
        for (byte timeIndex = 0; timeIndex < thisO.size(); timeIndex++) {
            for (Byte b : thisO.get(timeIndex).keySet()) {
                if (!otherO.get(timeIndex).containsKey(b)) {
                    return false; //2
                }
                String thisFluents = thisO.get(timeIndex).get(b);
                String otherFluents = otherO.get(timeIndex).get(b);
                byte result = Fluents.fluentsIsIn(thisFluents, otherFluents);
                if (result != 0) {
                    if (result == 1) {
                        equals = false;
                    } else if (result == 2) {
                        return false; //2 -
                    }
                }
            }
        }
        return !equals;
    }

    public void hPreserveFluentsAtTime(byte newTimeIndex) {
        String oldTimeFluents = this.sysElemH.get(newTimeIndex - 1);
        String newTimeFluents = this.sysElemH.get(newTimeIndex);

        StringBuilder resultSB = new StringBuilder("");
        for (int fluentIndex = 0; fluentIndex < newTimeFluents.length(); fluentIndex++) {
            if (newTimeFluents.charAt(fluentIndex) == '?') {
                resultSB.append(oldTimeFluents.charAt(fluentIndex));
            } else {
                resultSB.append(newTimeFluents.charAt(fluentIndex));
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
                if (otherN.get(timeIndex).actionID != -1) {
                    equals = false;
                }
            } else {
                if (!thisN.get(timeIndex).actionID.equals(otherN.get(timeIndex).actionID)) {
                    return false; //different
                }
            }
        }
        return !equals;
    }

    public boolean eIsAgentInvolvedAtTime(String agentToCheck, byte timeID, ArrayList<String> actionsMapping) {
        SysElemEAtTimeUnit e = this.sysElemE.get(timeID);
        byte actionID = e.occuringAction;
        if (actionID == -1) {
            return false;
        }
        String actionString = actionsMapping.get(actionID);
        return Action.isAgentInActionString(actionString, agentToCheck);
    }

    public boolean eIsIn(ArrayList<SysElemEAtTimeUnit> otherE) {
        boolean equals = true;
        ArrayList<SysElemEAtTimeUnit> thisE = this.sysElemE;
        for (byte timeIndex = 0; timeIndex < thisE.size(); timeIndex++) {
            if (thisE.get(timeIndex).occuringAction == -1) {
                if (otherE.get(timeIndex).occuringAction != -1) {
                    equals = false;
                }
            } else {
                if (thisE.get(timeIndex).occuringAction != otherE.get(timeIndex).occuringAction) {
                    return false; //different
                }
            }
        }
        return !equals;
    }

    //ArrayList<HashMap<Byte, String>> sysElemO; //timeID actionID fluents
    public boolean oAreSysElemOsTheSame(ArrayList<HashMap<Byte, String>> otherSysElemO) {
        for (byte index = 0; index < this.sysElemN.size(); index++) {
            if (this.sysElemO.get(index).keySet().size() != 0 ||
                    otherSysElemO.get(index).keySet().size() != 0) {
                if (this.sysElemO.get(index).keySet().size() == 0 ||
                        otherSysElemO.get(index).keySet().size() == 0) {
                    return false;
                }
                //compare actionID
                else if (this.sysElemO.get(index).keySet().iterator().next().equals(otherSysElemO.get(index).keySet().iterator().next())) { //20150915
                    byte thisActionIDAtTime = this.sysElemO.get(index).keySet().iterator().next();
                    if (!this.sysElemO.get(index).get(thisActionIDAtTime).equals(
                            otherSysElemO.get(index).get(thisActionIDAtTime))) {
                                return false;
                            }
                } else {
                    return false;
                }
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

    //ArrayList<SysElemEAtTimeUnit> sysElemE
    public boolean eAreSysElemEsTheSame(ArrayList<SysElemEAtTimeUnit> otherSysElemE) {
        for (byte index = 0; index < this.sysElemN.size(); index++) {
            if (!this.sysElemE.get(index).areSame(otherSysElemE.get(index))) {
                return false;
            }
        }
        return true;
    }

    public boolean isFullTime(byte timeID) {
        return timeID >= tMax;
    }

    public void addTypicalActionIndex(byte newIndex) {
        if (this.firstTypicalActionIndex == -1) {
            this.firstTypicalActionIndex = newIndex;
        } else {
            this.firstTypicalActionIndex = (byte) Math.min(this.firstTypicalActionIndex, newIndex);
        }
    }

    public boolean isActionTypicalAtTime(byte timeIndex) {
        //return true;
        return this.firstTypicalActionIndex != -1 && timeIndex >= this.firstTypicalActionIndex;
    }

    public boolean isStateTypicalAtTime(byte timeIndex) {
        //return true;
        return this.firstTypicalActionIndex != -1 && timeIndex > this.firstTypicalActionIndex;
    }

    public boolean aAreSysElemAsTheSame(ArrayList<SysElemAAtTimeUnit> otherSysElemA) {
        for (byte index = 0; index < this.sysElemA.size(); index++) {
            if (!this.sysElemA.get(index).areSame(otherSysElemA.get(index))) {
                return false;
            }
        }
        return true;
    }

    public void aAddAtypicalAction(byte timeID, byte actionID) {
        if (timeID >= tMax) {
            return; //20150905
        }
        ArrayListOfByteUtils.insertIntoArrayList(this.sysElemA.get(timeID).actionIDs, actionID);
    }

    public int aCountAs() {
        int result = 0;
        for (byte timeIndex = 0; timeIndex < tMax; timeIndex++) {
            SysElemAAtTimeUnit sysElemA = this.sysElemA.get(timeIndex);
            result += sysElemA.actionIDs.size();
        }
        return result;
    }

    public byte getErrorInsertingActionAtTime() {
        return this.errorInsertingActionAtTime;
    }

    public void addErrorInsertingActionAtTime(byte newTime) {
        if (this.errorInsertingActionAtTime == -1) {
            this.errorInsertingActionAtTime = newTime;
        } else {
            this.errorInsertingActionAtTime = (byte) Math.min(this.errorInsertingActionAtTime, newTime);
        }
    }

}
