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
public class CausesSentence extends Sentence {

    private static final Logger log = LoggerFactory.getLogger(CausesSentence.class);

    public Action action;
    public IFormula causesFormula;
    public IFormula conditionFormula;

    public CausesSentence(Action action, IFormula causesFormula, IFormula conditionFormula, ActionDomain actionDomain) {
        this.action = action;
        this.causesFormula = causesFormula;
        this.conditionFormula = conditionFormula;

        super.addFluentsToActionDomain(causesFormula.getFluents(), actionDomain);
        if (conditionFormula != null) {
            super.addFluentsToActionDomain(conditionFormula.getFluents(), actionDomain);
        }
        super.addActionToActionDomain(action, actionDomain);
    }

    @Override
    public boolean isTypical() {
        return false;
    }

    @Override
    public void fillFluentAndActionIDs(ArrayList<String> fluentMappings, ArrayList<String> actionMappings) {
        causesFormula.fillFluentsIDs(fluentMappings);
        if (conditionFormula != null) {
            conditionFormula.fillFluentsIDs(fluentMappings);
        }
        action.fillFluentsIDs(actionMappings);
    }

    @Override
    public String toString() {
        return "[" + action.toString() + " causes [" + causesFormula.toString() + "] if [" +
                StringUtils.TSIN(conditionFormula) + "]]";
    }

    @Override
    public ArrayList<Hoent> applyCertainSentence(ArrayList<Hoent> structures, byte fluentsCount, byte timeID,
                                                 boolean secondPass, HoentsSettings hoentsSettings)
            throws Exception{
        //A causes a if p
        ArrayList<Hoent> newStructures = new ArrayList<Hoent>();

        if (this.conditionFormula != null) {
            ArrayList<ArrayList<String>> posAndNegEvaluates =
                    FormulaUtils.getPositiveAndNegativeEvaluates(this.conditionFormula, fluentsCount);
            ArrayList<String> posEvaluates = posAndNegEvaluates.get(0); //e.g., ?100? [fluentIDs: 2,3,4; negations: 0,1,1; fluentCount: 5]
            ArrayList<String> negEvaluates = posAndNegEvaluates.get(1);
            //ArrayList<String> negEvaluates = posAndNegEvaluates.get(1);

            for (Hoent structure : structures) {
                boolean isAtLeastOneNewStructure = false;
                //boolean addedIdenticalStructure = false;

                //newStructures.add(structure.copy()); //20150905
                if (!structure.eIsActionAtTime(this.action.actionID, timeID)) {
                    newStructures.add(structure.copy()); //20150905
                    continue;
                }

                new CommonMethod(timeID, newStructures, negEvaluates, structure).invoke();

                for (String posEvaluate : posEvaluates) {
                    //boolean leftConditions = true;

                    boolean hCompatibility = structure.hCheckCompatibility(posEvaluate, timeID);
                    if (hCompatibility == false) {
                        //newStructures.add(structure.copy()); //20150905 //20150911
                        continue;
                    }
                    String newEvaluates = structure.hGetNewEvaluates(posEvaluate, timeID);

                    Hoent newStructure = structure.copy();
                    newStructure.hAddNewEvaluates(newEvaluates, timeID); //ifCondition

                    if (structure.isFullTime((byte)(timeID + 1))) { //20150910
                        Hoent newNewStructure = newStructure.copy();
                        newNewStructure.hasExceededTimeLimit = true;
                        newStructures.add(newNewStructure);
                        continue;
                    }

                    //resultCondition
                    ArrayList<ArrayList<String>> posAndNegEvaluatesOfResultCondition =
                            FormulaUtils.getPositiveAndNegativeEvaluates(this.causesFormula, fluentsCount);
                    ArrayList<String> posEvaluatesOfResultCondition = posAndNegEvaluatesOfResultCondition.get(0); //e.g., ?100? [fluentIDs: 2,3,4; negations: 0,1,1; fluentCount: 5]
                    someMethod(timeID, secondPass, hoentsSettings, newStructures, structure, newStructure, false, posEvaluatesOfResultCondition, false);
                }
            }
        }
        else {
            //empty if part=================================================================================================
            //A causes a if p

            for (Hoent structure : structures) {

                if(!structure.eIsActionAtTime(this.action.actionID, timeID)) {
                    newStructures.add(structure.copy()); //change
                    continue;
                }

                Hoent newStructure = structure.copy();

                if (structure.isFullTime((byte)(timeID + 1))) { //20150910
                    Hoent newNewStructure = newStructure.copy();
                    newNewStructure.hasExceededTimeLimit = true;
                    newStructures.add(newNewStructure);
                    continue;
                }

                //resultCondition
                ArrayList<ArrayList<String>> posAndNegEvaluatesOfResultCondition =
                        FormulaUtils.getPositiveAndNegativeEvaluates(this.causesFormula, fluentsCount);
                ArrayList<String> posEvaluatesOfResultCondition = posAndNegEvaluatesOfResultCondition.get(0); //e.g., ?100? [fluentIDs: 2,3,4; negations: 0,1,1; fluentCount: 5]
                someMethod(timeID, secondPass, hoentsSettings, newStructures, structure, newStructure, false, posEvaluatesOfResultCondition, false);
            }
        }

        if (newStructures.size() == 0) {
            throw new Exception("Zero HOENTs (contradictory action domain) after sentence: [" + this.toString() + "] at time =[" + timeID + "]." );
        }

        return newStructures;
    }

    private void someMethod(byte timeID, boolean secondPass, HoentsSettings hoentsSettings, ArrayList<Hoent> newStructures, Hoent structure, Hoent newStructure, boolean isAtLeastOneSameResultingStructure, ArrayList<String> posEvaluatesOfResultCondition, boolean isAtLEastOnePosResultEvalCompatible) throws Exception {
        for (String posEvaluateOfResultCondition : posEvaluatesOfResultCondition) {
            boolean hCompatibilityOfResCond = newStructure.hCheckCompatibility(posEvaluateOfResultCondition,
                    (byte) (timeID + 1));
            if (!hCompatibilityOfResCond) {
                continue;
            }
            isAtLEastOnePosResultEvalCompatible = true;
            String newEvaluatesOfResultCondition = newStructure.hGetNewEvaluates(posEvaluateOfResultCondition,
                    (byte) (timeID + 1));
            byte zerosAndOnesCounterOfResultCondition = StringUtils.countZerosAndOnes(newEvaluatesOfResultCondition);
            if (zerosAndOnesCounterOfResultCondition == 0) {
                if (!isAtLeastOneSameResultingStructure) {
                    Hoent newStructureOfResultCondition = newStructure.copy();
                    newStructureOfResultCondition.oAddFluents(this.action.actionID, this.causesFormula.getFluentsIDs(), (byte) (timeID));
                    newStructures.add(newStructureOfResultCondition);
                }
                isAtLeastOneSameResultingStructure = true;
                continue;
            }
            Hoent newStructureOfResultCondition = newStructure.copy();
            newStructureOfResultCondition.hAddNewEvaluates(newEvaluatesOfResultCondition, (byte) (timeID + 1));
            newStructureOfResultCondition.oAddFluents(this.action.actionID, this.causesFormula.getFluentsIDs(), (byte) (timeID));
            newStructures.add(newStructureOfResultCondition);
        }
        if (!isAtLEastOnePosResultEvalCompatible) {
            String message = "Error in applying sentence: [" + this.toString() + "] - can't apply resulting condition at time [" + Integer.toString(timeID + 1) + "] secondPass==[" + secondPass + "]."; //20150906
            log.debug(message);
            if (hoentsSettings.isDoThrow() && !secondPass && !structure.isActionTypicalAtTime(timeID)) { //
                throw new Exception(message);
            }
        }
    }
}
