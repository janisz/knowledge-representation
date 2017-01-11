package pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentences;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.ActionDomain;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts.Action;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts.FormulaUtils;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts.IFormula;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts.Time;
import pl.edu.pw.mini.msi.knowledgerepresentation.hoents.Hoent;
import pl.edu.pw.mini.msi.knowledgerepresentation.hoents.HoentsSettings;
import pl.edu.pw.mini.msi.knowledgerepresentation.utils.StringUtils;

import java.util.ArrayList;

/**
 * Created by Tomek on 2015-08-29.
 */
public class InvokesSentence extends Sentence {

    private static final Logger log = LoggerFactory.getLogger(TriggersSentence.class);

    public boolean typically;
    public Action causalAction;
    public Action resultingAction;
    public Time time;
    public IFormula conditionFormula;

    public InvokesSentence(boolean typically, Action causalAction, Action resultingAction, Time time,
                           IFormula conditionFormula, ActionDomain actionDomain) {
        this.typically = typically;
        this.causalAction = causalAction;
        this.resultingAction = resultingAction;
        this.time = time;
        this.conditionFormula = conditionFormula;

        if (time == null) {
            this.time = new Time("1");
        }

        if (conditionFormula != null) {
            super.addFluentsToActionDomain(conditionFormula.getFluents(), actionDomain);
        }
        super.addActionToActionDomain(causalAction, actionDomain);
        super.addActionToActionDomain(resultingAction, actionDomain);
    }

    @Override
    public boolean isTypical() {
        return typically;
    }

    @Override
    public void fillFluentAndActionIDs(ArrayList<String> fluentMappings, ArrayList<String> actionMappings) {
        if (conditionFormula != null) {
            conditionFormula.fillFluentsIDs(fluentMappings);
        }
        causalAction.fillFluentsIDs(actionMappings);
        resultingAction.fillFluentsIDs(actionMappings);
    }

    @Override
    public String toString() {
        String typicallyString = StringUtils.booleanTypicallyToString(typically);
        return "[" + typicallyString + causalAction.toString() + " invokes " + resultingAction.toString() +
                "after " + time.toString() + " if [" + StringUtils.TSIN(conditionFormula) + "]]";
    }

    @Override
    public ArrayList<Hoent> applyCertainSentence(ArrayList<Hoent> structures, byte fluentsCount, byte timeID,
                                                 boolean secondPass, HoentsSettings hoentsSettings)
            throws Exception{
        //A invokes B after t if p
        ArrayList<Hoent> newStructures = new ArrayList<Hoent>();

        if (this.conditionFormula != null) {
            ArrayList<ArrayList<String>> posAndNegEvaluates =
                    FormulaUtils.getPositiveAndNegativeEvaluates(this.conditionFormula, fluentsCount);
            ArrayList<String> posEvaluates = posAndNegEvaluates.get(0); //e.g., ?100? [fluentIDs: 2,3,4; negations: 0,1,1; fluentCount: 5]
            ArrayList<String> negEvaluates = posAndNegEvaluates.get(1);

            for (Hoent structure : structures) {
                if (!structure.eIsActionAtTime(this.causalAction.actionID, timeID)) {
                    newStructures.add(structure.copy()); //20150905
                    continue;
                }

                new CommonMethod(timeID, newStructures, negEvaluates, structure).invoke();

                for (String posEvaluate : posEvaluates) {
                    boolean hCompatibility = structure.hCheckCompatibility(posEvaluate, timeID);
                    if (!hCompatibility) {
                        continue;
                    }
                    String newEvaluates = structure.hGetNewEvaluates(posEvaluate, timeID);
                    Hoent newStructure = structure.copy();
                    newStructure.hAddNewEvaluates(newEvaluates, timeID); //ifCondition

                    if (structure.isFullTime((byte)(timeID + this.time.timeID))) { //20150909
                        Hoent newNewStructure = newStructure.copy();
                        newNewStructure.hasExceededTimeLimit = true;
                        newStructures.add(newNewStructure);
                        continue;
                    }

                    if (this.resultingAction.task.negated) {
                        Hoent newNewStructure = structure.copy();
                        if (!newNewStructure.eAddNegatedActionAtTime(this.resultingAction.actionID,
                                (byte) (timeID + this.time.timeID))){
                            String message = "Error in applying sentence: [" + this.toString() + "] - can't insert resulting action at time [" + Integer.toString(timeID + this.time.timeID) + "] secondPass==[" + secondPass + "]."; //20150906
                            log.debug(message);
                            newNewStructure.addErrorInsertingActionAtTime( (byte)(timeID + this.time.timeID) ); //20150913
                        }
                        //newNewStructure.hAddNewEvaluates(newEvaluates, timeID); //20150906_3
                        newStructures.add(newNewStructure);
                        continue;
                    }

                    if (!newStructure.eCanInsertActionAtTime(this.resultingAction.actionID, (byte) (timeID + this.time.timeID))) {
                        String message = "Error in applying sentence: [" + this.toString() + "] - can't insert resulting action at time [" + Integer.toString(timeID + this.time.timeID) + "] secondPass==[" + secondPass + "]."; //20150906
                        log.debug(message);
                        newStructure.addErrorInsertingActionAtTime( (byte)(timeID + this.time.timeID) ); //20150913
                    }
                    newStructure.eAddAction(this.resultingAction.actionID, (byte)(timeID + this.time.timeID));

                    newStructures.add(newStructure);
                }
            }
        }
        else {
            for (Hoent structure : structures) {
                if(!structure.eIsActionAtTime(this.causalAction.actionID, timeID)) {
                    newStructures.add(structure.copy());
                    continue;
                }

                if (structure.isFullTime((byte)(timeID + this.time.timeID))) { //20150909
                    Hoent newStructure = structure.copy();
                    newStructure.hasExceededTimeLimit = true;
                    newStructures.add(newStructure);
                    continue;
                }

                Hoent newStructure = structure.copy();

                if (this.resultingAction.task.negated) {
                    Hoent newNewStructure = structure.copy();
                    if (!newNewStructure.eCanAddNegatedActionAtTime(this.resultingAction.actionID,
                            (byte) (timeID + this.time.timeID))){
                        String message = "Error in applying sentence: [" + this.toString() + "] - can't insert resulting action at time [" + Integer.toString(timeID + this.time.timeID) + "] secondPass==[" + secondPass + "]."; //20150906
                        log.debug(message);
                        newNewStructure.addErrorInsertingActionAtTime( (byte)(timeID + this.time.timeID) ); //20150913
                    }
                    newNewStructure.eAddNegatedActionAtTime(this.resultingAction.actionID, (byte)(timeID + this.time.timeID));
                    newStructures.add(newNewStructure);
                    continue;
                }

                if (!newStructure.eCanInsertActionAtTime(this.resultingAction.actionID, (byte) (timeID + this.time.timeID))) {
                    String message = "Error in applying sentence: [" + this.toString() + "] - can't insert resulting action at time [" + Integer.toString(timeID + this.time.timeID) + "] secondPass==[" + secondPass + "]."; //20150906
                    log.debug(message);
                    newStructure.addErrorInsertingActionAtTime( (byte)(timeID + this.time.timeID) ); //20150913
                }
                newStructure.eAddAction(this.resultingAction.actionID, (byte) (timeID + this.time.timeID));

                newStructures.add(newStructure);
            }
        }

        return newStructures;
    }

    @Override
    public ArrayList<Hoent> applyTypicalSentence(ArrayList<Hoent> structures, byte fluentsCount, byte timeID,
                                                 boolean secondPass, HoentsSettings hoentsSettings)
            throws Exception{
        //A invokes B after t if p
        ArrayList<Hoent> newStructures = new ArrayList<Hoent>();

        if (this.conditionFormula != null) {
            ArrayList<ArrayList<String>> posAndNegEvaluates =
                    FormulaUtils.getPositiveAndNegativeEvaluates(this.conditionFormula, fluentsCount);
            ArrayList<String> posEvaluates = posAndNegEvaluates.get(0); //e.g., ?100? [fluentIDs: 2,3,4; negations: 0,1,1; fluentCount: 5]
            ArrayList<String> negEvaluates = posAndNegEvaluates.get(1);

            for (Hoent structure : structures) {
                if (secondPass) { //20150906
                    continue;
                }

                if (!structure.eIsActionAtTime(this.causalAction.actionID, timeID)) {
                    newStructures.add(structure.copy());
                    continue;
                }

                new CommonMethod(timeID, newStructures, negEvaluates, structure).invoke();

                for (String posEvaluate : posEvaluates) {
                    boolean hCompatibility = structure.hCheckCompatibility(posEvaluate, timeID);
                    if (!hCompatibility) {
                        continue;
                    }
                    String newEvaluates = structure.hGetNewEvaluates(posEvaluate, timeID);
                    byte zerosAndOnesCounter = StringUtils.countZerosAndOnes(newEvaluates);
                    Hoent newStructure = structure.copy();
                    newStructure.hAddNewEvaluates(newEvaluates, timeID); //ifCondition

                    if (structure.isFullTime((byte)(timeID + this.time.timeID))) { //20150909
                        Hoent newNewStructure = newStructure.copy();
                        newNewStructure.hasExceededTimeLimit = true;
                        newStructures.add(newNewStructure);
                        continue;
                    }

                    newStructure.addTypicalActionIndex((byte) (timeID + this.time.timeID)); //20150911
                    if (!newStructure.eIsActionAtTime(this.resultingAction.actionID, (byte) (timeID + this.time.timeID))) {
                        Hoent newAStructure = newStructure.copy();
                        newAStructure.aAddAtypicalAction((byte) (timeID + this.time.timeID), this.resultingAction.actionID);
                        newStructures.add(newAStructure); //TODO TOMEKL important; not typically resulting action wasn't invoked
                    }

                    newStructure.nSetToTrue((byte)(timeID + this.time.timeID), this.resultingAction.actionID);

                    if (!newStructure.eCanInsertActionAtTime(this.resultingAction.actionID, (byte) (timeID + this.time.timeID))) {
                        //change compared to applyCertainSentence
                        continue; //TODO TOMEKL throw error information?
                        //throw new Exception("Error in applying sentence: [" + this.toString() + "] - can't insert resulting action.");
                    }
                    newStructure.eAddAction(this.resultingAction.actionID, (byte)(timeID + this.time.timeID));

                    newStructures.add(newStructure);
                }
            }
        }
        else {
            //empty if part=================================================================================================
            //A invokes B after t if p

            for (Hoent structure : structures) {
                if(!structure.eIsActionAtTime(this.causalAction.actionID, timeID)) {
                    newStructures.add(structure.copy());
                    continue;
                }

                Hoent newStructure = structure.copy();
                //newStructure.hAddNewEvaluates(newEvaluates, timeID); //ifCondition

                if (structure.isFullTime((byte)(timeID + this.time.timeID))) { //20150909
                    Hoent newNewStructure = newStructure.copy();
                    newNewStructure.hasExceededTimeLimit = true;
                    newStructures.add(newNewStructure);
                    continue;
                }

                newStructure.addTypicalActionIndex((byte)(timeID + this.time.timeID));
                if (!newStructure.eIsActionAtTime(this.resultingAction.actionID, (byte) (timeID + this.time.timeID))) {
                    Hoent newAStructure = structure.copy();
                    newAStructure.aAddAtypicalAction((byte) (timeID + this.time.timeID), this.resultingAction.actionID);
                    newStructures.add(newAStructure); //important; not typically resulting action wasn't invoked
                }

                if (!newStructure.eCanInsertActionAtTime(this.resultingAction.actionID, (byte) (timeID + this.time.timeID))) {
                    //change compared to applyCertainSentence
                    continue; //TODO TOMEKL throw error information?
                    //throw new Exception("Error in applying sentence: [" + this.toString() + "] - can't insert resulting action.");
                }
                newStructure.eAddAction(this.resultingAction.actionID, (byte)(timeID + this.time.timeID));
                newStructure.nSetToTrue((byte) (timeID + this.time.timeID), this.resultingAction.actionID);

                newStructures.add(newStructure);

                //newStructures.add(structure.copy()); //TODO comment this?
            }
        }

        return newStructures;
    }
}
