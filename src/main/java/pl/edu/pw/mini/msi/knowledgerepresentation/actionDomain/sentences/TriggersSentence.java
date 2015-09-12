package pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentences;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.ActionDomain;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts.Action;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts.FormulaUtils;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts.IFormula;
import pl.edu.pw.mini.msi.knowledgerepresentation.hoents.Hoent;
import pl.edu.pw.mini.msi.knowledgerepresentation.hoents.HoentsSettings;
import pl.edu.pw.mini.msi.knowledgerepresentation.utils.StringUtils;

import java.util.ArrayList;

/**
 * Created by Tomek on 2015-08-29.
 */
public class TriggersSentence extends Sentence {
    public boolean typically;
    public IFormula conditionFormula;
    public Action action;

    private static final Logger log = LoggerFactory.getLogger(TriggersSentence.class);

    public TriggersSentence(boolean typically, IFormula conditionFormula, Action action, ActionDomain actionDomain) {
        this.typically = typically;
        this.conditionFormula = conditionFormula;
        this.action = action;

        super.addFluentsToActionDomain(conditionFormula.getFluents(), actionDomain);
        super.addActionToActionDomain(action, actionDomain);
    }

    @Override
    public boolean isTypical() {
        return typically;
    }

    @Override
    public void fillFluentAndActionIDs(ArrayList<String> fluentMappings, ArrayList<String> actionMappings) {
        conditionFormula.fillFluentsIDs(fluentMappings);
        action.fillFluentsIDs(actionMappings);
    }

    @Override
    public String toString() {
        String typicallyString = StringUtils.booleanTypicallyToString(typically);
        return "[" + typicallyString + "[" +  conditionFormula.toString() + " triggers " + action.toString() + "]";
    }

    @Override
    public ArrayList<Hoent> applyCertainSentence(ArrayList<Hoent> structures, byte fluentsCount, byte timeID,
                                                 boolean secondPass, HoentsSettings hoentsSettings)
            throws Exception{
        //p triggers A
        ArrayList<Hoent> newStructures = new ArrayList<Hoent>();

        ArrayList<ArrayList<String>> posAndNegEvaluates =
                FormulaUtils.getPositiveAndNegativeEvaluates(this.conditionFormula, fluentsCount);
        ArrayList<String> posEvaluates = posAndNegEvaluates.get(0); //e.g., ?100? [fluentIDs: 2,3,4; negations: 0,1,1; fluentCount: 5]
        ArrayList<String> negEvaluates = posAndNegEvaluates.get(1);

        for (Hoent structure : structures) {
            boolean isAtLeastOneNewStructure = false;
            //boolean addedIdenticalStructure = false;

            //newStructures.add(structure.copy()); //important //20150905

            //if (posEvaluates.size() == 0) { //20150906 //20150911
            //    newStructures.add(structure.copy());
            //    continue;
            //}
            for (String negEvaluate : negEvaluates) { //20150911

                boolean hCompatibility = structure.hCheckCompatibility(negEvaluate, timeID);
                if (hCompatibility == false) {
                    //newStructures.add(structure.copy());
                    continue;
                }
                String newEvaluates = structure.hGetNewEvaluates(negEvaluate, timeID);
                //byte zerosAndOnesCounter = StringUtils.countZerosAndOnes(newEvaluates);
                //if (zerosAndOnesCounter == 0) {
                //    //newStructures.add(structure.copy());
                //    continue;
                //}
                //byte zerosAndOnesCounter = StringUtils.countZerosAndOnes(newEvaluates); //20150905_02
                //if (zerosAndOnesCounter != 0) { //20150905_02
                //    newStructures.add(structure.copy()); //add hoent with "?'s" //20150905_02
                //} //20150905_02
                Hoent newStructure = structure.copy();
                newStructure.hAddNewEvaluates(newEvaluates, timeID); //ifCondition
                newStructures.add(newStructure);
            }


            for (String posEvaluate : posEvaluates) {
                boolean leftConditions = true;

                if (structure.hCheckCompatibility(posEvaluate, timeID) == false) { //20150906
                    //newStructures.add(structure.copy()); //20150911
                    continue;
                }

                String newEvaluates = structure.hGetNewEvaluates(posEvaluate, timeID); //20150906_02
                byte zerosAndOnesCounter = StringUtils.countZerosAndOnes(newEvaluates); //20150906_02
                if (zerosAndOnesCounter != 0) { //20150906_02
                    //newStructures.add(structure.copy()); //add hoent with "?'s" //20150906_02 //20150911_add?
                } //20150905_02

                if (this.action.task.negated == true) {
                    Hoent newStructure = structure.copy();
                    newStructure.hAddNewEvaluates(newEvaluates, timeID); //20150906_3
                    if (newStructure.eCanAddNegatedActionAtTime(this.action.actionID, timeID) == false){
                        String message = "Error in applying sentence: [" + this.toString() + "] - can't insert resulting action at time [" + timeID + "] secondPass==[" + secondPass + "].";
                        log.debug(message);
                        if (hoentsSettings.isDoThrow() == true && structure.isActionTypicalAtTime(timeID) == false
                                && zerosAndOnesCounter == 0) { //
                            throw new Exception(message);
                        }
                        else {
                            continue; //not relevant
                        }
                    }
                    newStructure.eAddNegatedActionAtTime(this.action.actionID, timeID);
                    newStructures.add(newStructure);
                    continue;
                }

                //if (structure.eIsActionAtTime(this.action.actionID, timeID) == true) {
                //    Hoent newStructure = structure.copy();
                //    newStructure.hAddNewEvaluates(newEvaluates, timeID); //20150906_3
                //    newStructures.add(newStructure); //20150905
                //    continue;
                //}

                if (structure.eCanInsertActionAtTime(this.action.actionID, timeID) == false) {
                    //newStructures.add(structure.copy());
                    //continue; //TODO TOMEKL throw error information?
                    String message = "Error in applying sentence: [" + this.toString() + "] - can't insert resulting action at time [" + timeID + "] secondPass==[" + secondPass + "].";
                    log.debug(message);
                    if (hoentsSettings.isDoThrow() == true && structure.isActionTypicalAtTime(timeID) == false
                            && zerosAndOnesCounter == 0) { //
                        throw new Exception(message);
                    }
                    else {
                        continue; //not relevant
                    }
                }

                Hoent newStructure = structure.copy();
                newStructure.eAddAction(this.action.actionID, timeID);
                newStructure.hAddNewEvaluates(newEvaluates, timeID); //20150906_3
                newStructures.add(newStructure);

                //newStructures.add(newStructure);
                //leftConditions = leftConditions && structure.hCheckCompatibility(posEvaluate, timeID);
            }
        }


        if (newStructures.size() == 0) {
            throw new Exception("Zero HOENTs (contradictory action domain) after sentence: [" + this.toString() + "]");
        }

        return newStructures;
    }

    @Override
    public ArrayList<Hoent> applyTypicalSentence(ArrayList<Hoent> structures, byte fluentsCount, byte timeID,
                                                 boolean secondPass, HoentsSettings hoentsSettings)
            throws Exception{
        //p triggers A
        ArrayList<Hoent> newStructures = new ArrayList<Hoent>();

        ArrayList<ArrayList<String>> posAndNegEvaluates =
                FormulaUtils.getPositiveAndNegativeEvaluates(this.conditionFormula, fluentsCount);
        ArrayList<String> posEvaluates = posAndNegEvaluates.get(0); //e.g., ?100? [fluentIDs: 2,3,4; negations: 0,1,1; fluentCount: 5]
        ArrayList<String> negEvaluates = posAndNegEvaluates.get(1);

        for (Hoent structure : structures) {
            //boolean isAtLeastOneNewStructure = false;
            //boolean addedIdenticalStructure = false;

            //old place of atypical structure addition

            if (secondPass) { //20150906
                continue;
            }

            //if (posEvaluates.size() == 0) { //20150906 //20150910 //20150911
            //    newStructures.add(structure.copy());
            //    continue;
            //}
            for (String negEvaluate : negEvaluates) { //20150911

                boolean hCompatibility = structure.hCheckCompatibility(negEvaluate, timeID);
                if (hCompatibility == false) {
                    //newStructures.add(structure.copy());
                    continue;
                }
                String newEvaluates = structure.hGetNewEvaluates(negEvaluate, timeID);
                //byte zerosAndOnesCounter = StringUtils.countZerosAndOnes(newEvaluates);
                //if (zerosAndOnesCounter == 0) {
                //    //newStructures.add(structure.copy());
                //    continue;
                //}
                //byte zerosAndOnesCounter = StringUtils.countZerosAndOnes(newEvaluates); //20150905_02
                //if (zerosAndOnesCounter != 0) { //20150905_02
                //    newStructures.add(structure.copy()); //add hoent with "?'s" //20150905_02
                //} //20150905_02
                Hoent newStructure = structure.copy();
                newStructure.hAddNewEvaluates(newEvaluates, timeID); //ifCondition
                newStructures.add(newStructure);
            }

            for (String posEvaluate : posEvaluates) {

                //change compared to applyCertainSentence
                //if (this.action.task.negated == true) {
                //    Hoent newStructure = structure.copy();
                //    newStructure.eAddNegatedActionAtTime(this.action.actionID, timeID);
                //    newStructures.add(newStructure);
                //    continue;
                //}

                //if (structure.hCheckCompatibility(posEvaluate, timeID) == false) { //20150906
                //    newStructures.add(structure.copy());
                //    continue;
                //}

                if (structure.hCheckCompatibility(posEvaluate, timeID) == false) { //20150906_3 //20150910
                    //newStructures.add(structure.copy()); //20150911
                    continue;
                }

                String newEvaluates = structure.hGetNewEvaluates(posEvaluate, timeID); //20150906_02
                byte zerosAndOnesCounter = StringUtils.countZerosAndOnes(newEvaluates); //20150906_02
                if (zerosAndOnesCounter != 0) { //20150906_02
                    //newStructures.add(structure.copy()); //add hoent with "?'s" //20150906_02 //20150911_add?
                } //20150905_02

                //if (structure.eIsActionAtTime(this.action.actionID, timeID) == true) {
                    //newStructures.add(structure.copy());
                    //ARBITRARY WHAT TO DO IN SUCH A SITUATION
                //    continue;
                //}

                structure.addTypicalActionIndex(timeID);
                if (structure.eIsActionAtTime(this.action.actionID, timeID) == false) {
                    Hoent newAStructure = structure.copy();
                    newAStructure.hAddNewEvaluates(newEvaluates, timeID); //20150906_3
                    newAStructure.aAddAtypicalAction(timeID, this.action.actionID);
                    newStructures.add(newAStructure); //important; not typically, triggers didn't occur
                }

                if (structure.eCanInsertActionAtTime(this.action.actionID, timeID) == false) {
                    //newStructures.add(structure.copy());
                    //change compared to applyCertainSentence
                    continue;
                    //throw new Exception("Error in applying sentence: [" + this.toString() + "] - can't insert resulting action.");
                }

                Hoent newStructure = structure.copy();
                newStructure.hAddNewEvaluates(newEvaluates, timeID); //20150906_3
                newStructure.eAddAction(this.action.actionID, timeID);
                newStructure.nSetToTrue(timeID, this.action.actionID);
                newStructures.add(newStructure);

                //newStructures.add(newStructure);
                //leftConditions = leftConditions && structure.hCheckCompatibility(posEvaluate, timeID);
            }
        }

        if (newStructures.size() == 0) {
            throw new Exception("Zero HOENTs (contradictory action domain) after sentence: [" + this.toString() + "] at time [" + timeID + "] secondPass==[" + secondPass + "].");
        }

        return newStructures;
    }
}
