package pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentences;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.ActionDomain;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts.Action;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts.FormulaUtils;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts.IFormula;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts.Time;
import pl.edu.pw.mini.msi.knowledgerepresentation.hoents.Hoent;
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
    public ArrayList<Hoent> applyCertainSentence(ArrayList<Hoent> structures, byte fluentsCount, byte timeID)
            throws Exception{
        //A invokes B after t if p
        ArrayList<Hoent> newStructures = new ArrayList<Hoent>();

        if (this.conditionFormula != null) {
            ArrayList<ArrayList<String>> posAndNegEvaluates =
                    FormulaUtils.getPositiveAndNegativeEvaluates(this.conditionFormula, fluentsCount);
            ArrayList<String> posEvaluates = posAndNegEvaluates.get(0); //e.g., ?100? [fluentIDs: 2,3,4; negations: 0,1,1; fluentCount: 5]

            for (Hoent structure : structures) {
                boolean isAtLeastOneNewStructure = false;
                //boolean addedIdenticalStructure = false;

                //newStructures.add(structure.copy()); //TODO TOMEKL important //20150905

                if (structure.eIsActionAtTime(this.causalAction.actionID, timeID) == false) {
                    newStructures.add(structure.copy()); //20150905
                    continue;
                }

                if (posEvaluates.size() == 0) {
                   newStructures.add(structure.copy());
                }

                for (String posEvaluate : posEvaluates) {
                    boolean hCompatibility = structure.hCheckCompatibility(posEvaluate, timeID);
                    if (hCompatibility == false) {
                        newStructures.add(structure.copy()); //20150905
                        continue;
                    }
                    String newEvaluates = structure.hGetNewEvaluates(posEvaluate, timeID);
                    //byte zerosAndOnesCounter = StringUtils.countZerosAndOnes(newEvaluates);
                    //if (zerosAndOnesCounter == 0) {
                    //    //newStructures.add(structure.copy());
                    //    continue;
                    //}
                    byte zerosAndOnesCounter = StringUtils.countZerosAndOnes(newEvaluates); //20150905_02
                    if (zerosAndOnesCounter != 0) { //20150905_02
                        newStructures.add(structure.copy()); //add hoent with "?'s" //20150905_02
                    } //20150905_02
                    Hoent newStructure = structure.copy();
                    newStructure.hAddNewEvaluates(newEvaluates, timeID); //ifCondition

                    if (newStructure.eCanInsertActionAtTime(this.resultingAction.actionID, (byte)(timeID + this.time.timeID))
                        == false) {
                        //continue; //TODO TOMEKL throw error information?
                        String message = "Error in applying sentence: [" + this.toString() + "] - can't insert resulting action at time [" + new Integer(timeID + this.time.timeID).toString() + "."; //20150906
                        //throw new Exception(message); //20150906
                        log.debug(message);
                        continue;

                    }
                    newStructure.eAddAction(this.resultingAction.actionID, (byte)(timeID + this.time.timeID));

                    newStructures.add(newStructure);
                    //leftConditions = leftConditions && structure.hCheckCompatibility(posEvaluate, timeID);
                }
            }
        }
        else {
            //empty if part=================================================================================================
            //A invokes B after t if p

            for (Hoent structure : structures) {
                boolean isAtLeastOneNewStructure = false;
                //boolean addedIdenticalStructure = false;

                boolean leftConditions = true;

                if(structure.eIsActionAtTime(this.causalAction.actionID, timeID) == false) {
                    newStructures.add(structure.copy());
                    continue;
                }

                Hoent newStructure = structure.copy();
                //newStructure.hAddNewEvaluates(newEvaluates, timeID); //ifCondition

                if (newStructure.eCanInsertActionAtTime(this.resultingAction.actionID, (byte)(timeID + this.time.timeID))
                        == false) {
                    //continue; //TODO TOMEKL throw error information?
                    String message = "Error in applying sentence: [" + this.toString() + "] - can't insert resulting action at time [" + new Integer(timeID + this.time.timeID).toString() + "."; //20150906
                    //throw new Exception(message); //20150906
                    log.debug(message);
                    continue;
                }
                newStructure.eAddAction(this.resultingAction.actionID, (byte) (timeID + this.time.timeID));

                newStructures.add(newStructure);

                //newStructures.add(structure.copy()); //TODO comment this?
            }
        }

        return newStructures;
    }

    @Override
    public ArrayList<Hoent> applyTypicalSentence(ArrayList<Hoent> structures, byte fluentsCount, byte timeID,
                                                 boolean secondPass)
            throws Exception{
        //A invokes B after t if p
        ArrayList<Hoent> newStructures = new ArrayList<Hoent>();

        if (this.conditionFormula != null) {
            ArrayList<ArrayList<String>> posAndNegEvaluates =
                    FormulaUtils.getPositiveAndNegativeEvaluates(this.conditionFormula, fluentsCount);
            ArrayList<String> posEvaluates = posAndNegEvaluates.get(0); //e.g., ?100? [fluentIDs: 2,3,4; negations: 0,1,1; fluentCount: 5]

            for (Hoent structure : structures) {
                boolean isAtLeastOneNewStructure = false;
                //boolean addedIdenticalStructure = false;

                newStructures.add(structure.copy()); //TODO TOMEKL important; not typically resulting action wasn't invoked
                if (secondPass) { //20150906
                    continue;
                }

                if (structure.eIsActionAtTime(this.causalAction.actionID, timeID) == false) {
                    continue;
                }

                for (String posEvaluate : posEvaluates) {
                    boolean hCompatibility = structure.hCheckCompatibility(posEvaluate, timeID);
                    if (hCompatibility == false) {
                        //newStructures.add(structure.copy());
                        continue;
                    }
                    String newEvaluates = structure.hGetNewEvaluates(posEvaluate, timeID);
                    //byte zerosAndOnesCounter = StringUtils.countZerosAndOnes(newEvaluates);
                    //if (zerosAndOnesCounter == 0) {
                    //    //newStructures.add(structure.copy());
                    //    continue;
                    //}
                    Hoent newStructure = structure.copy();
                    newStructure.hAddNewEvaluates(newEvaluates, timeID); //ifCondition
                    newStructure.nSetToTrue((byte)(timeID + this.time.timeID), this.resultingAction.actionID);

                    if (newStructure.eCanInsertActionAtTime(this.resultingAction.actionID, (byte)(timeID + this.time.timeID))
                            == false) {
                        //change compared to applyCertainSentence
                        continue; //TODO TOMEKL throw error information?
                        //throw new Exception("Error in applying sentence: [" + this.toString() + "] - can't insert resulting action.");
                    }
                    newStructure.eAddAction(this.resultingAction.actionID, (byte)(timeID + this.time.timeID));

                    newStructures.add(newStructure);
                    //leftConditions = leftConditions && structure.hCheckCompatibility(posEvaluate, timeID);
                }
            }
        }
        else {
            //empty if part=================================================================================================
            //A invokes B after t if p

            for (Hoent structure : structures) {
                boolean isAtLeastOneNewStructure = false;
                //boolean addedIdenticalStructure = false;

                newStructures.add(structure.copy());//not typically resulting action wasn't invoked
                if(structure.eIsActionAtTime(this.causalAction.actionID, timeID) == false) {
                    continue;
                }

                Hoent newStructure = structure.copy();
                //newStructure.hAddNewEvaluates(newEvaluates, timeID); //ifCondition

                if (newStructure.eCanInsertActionAtTime(this.resultingAction.actionID, (byte)(timeID + this.time.timeID))
                        == false) {
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
