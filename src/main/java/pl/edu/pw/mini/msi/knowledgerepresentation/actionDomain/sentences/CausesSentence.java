package pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentences;

import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.ActionDomain;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts.Action;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts.FormulaUtils;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts.IFormula;
import pl.edu.pw.mini.msi.knowledgerepresentation.hoents.Hoent;
import pl.edu.pw.mini.msi.knowledgerepresentation.utils.StringUtils;

import java.util.ArrayList;

/**
 * Created by Tomek on 2015-08-29.
 */
public class CausesSentence extends Sentence {
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
        conditionFormula.fillFluentsIDs(fluentMappings);
        action.fillFluentsIDs(actionMappings);
    }

    @Override
    public String toString() {
        return "[" + action.toString() + " causes [" + causesFormula.toString() + "] if [" +
                StringUtils.TSIN(conditionFormula) + "]]";
    }

    public ArrayList<Hoent> applyCertainSentence(ArrayList<Hoent> structures, byte fluentsCount, byte timeID){
        //A causes a if p
        ArrayList<Hoent> newStructures = new ArrayList<Hoent>();

        if (this.conditionFormula != null) {
            ArrayList<ArrayList<String>> posAndNegEvaluates =
                    FormulaUtils.getPositiveAndNegativeEvaluates(this.conditionFormula, fluentsCount);
            ArrayList<String> posEvaluates = posAndNegEvaluates.get(0); //e.g., ?100? [fluentIDs: 2,3,4; negations: 0,1,1; fluentCount: 5]

            for (Hoent structure : structures) {
                boolean isAtLeastOneNewStructure = false;
                //boolean addedIdenticalStructure = false;
                for (String posEvaluate : posEvaluates) {
                    boolean leftConditions = true;

                    leftConditions = leftConditions && structure.eIsActionAtTime(this.action.actionID, timeID);
                    if (leftConditions == false) {
                        //newStructures.add(structure.copy());
                        continue;
                    }

                    boolean hCompatibility = structure.hCheckCompatibility(posEvaluate, timeID);
                    if (hCompatibility == false) {
                        //newStructures.add(structure.copy());
                        continue;
                    }
                    String newEvaluates = structure.hGetNewEvaluates(posEvaluate, timeID);
                    byte zerosAndOnesCounter = StringUtils.countZerosAndOnes(newEvaluates);
                    if (zerosAndOnesCounter == 0) {
                        //newStructures.add(structure.copy());
                        continue;
                    }
                    Hoent newStructure = structure.copy();
                    newStructure.hAddNewEvaluates(newEvaluates, timeID); //ifCondition

                    //resultCondition
                    boolean isAtLeastOneSameResultingStructure = false;
                    boolean isAtLeastOneResultingStructure = false;
                    ArrayList<ArrayList<String>> posAndNegEvaluatesOfResultCondition =
                            FormulaUtils.getPositiveAndNegativeEvaluates(this.causesFormula, fluentsCount);
                    ArrayList<String> posEvaluatesOfResultCondition = posAndNegEvaluatesOfResultCondition.get(0); //e.g., ?100? [fluentIDs: 2,3,4; negations: 0,1,1; fluentCount: 5]
                    for (String posEvaluateOfResultCondition : posEvaluatesOfResultCondition) {
                        boolean hCompatibilityOfResCond = newStructure.hCheckCompatibility(posEvaluateOfResultCondition,
                                (byte) (timeID + 1));
                        if (hCompatibilityOfResCond == false) {
                            continue;
                        }
                        String newEvaluatesOfResultCondition = newStructure.hGetNewEvaluates(posEvaluateOfResultCondition,
                                (byte) (timeID + 1));
                        byte zerosAndOnesCounterOfResultCondition = StringUtils.countZerosAndOnes(newEvaluatesOfResultCondition);
                        if (zerosAndOnesCounterOfResultCondition == 0) {
                            if (isAtLeastOneSameResultingStructure == false) {
                                Hoent newStructureOfResultCondition = newStructure.copy();
                                newStructureOfResultCondition.oAddFluents(this.causesFormula.getFluentsIDs(), (byte) (timeID + 1));
                                newStructures.add(newStructureOfResultCondition);
                            }
                            isAtLeastOneSameResultingStructure = true;
                            isAtLeastOneResultingStructure = true;
                            continue;
                        }
                        Hoent newStructureOfResultCondition = newStructure.copy();
                        newStructureOfResultCondition.hAddNewEvaluates(newEvaluatesOfResultCondition, (byte) (timeID + 1));
                        newStructureOfResultCondition.oAddFluents(this.causesFormula.getFluentsIDs(), (byte) (timeID + 1));
                        newStructures.add(newStructureOfResultCondition);
                        isAtLeastOneResultingStructure = true;
                    }
                    //if (isAtLeastOneResultingStructure == false) {
                    //    throw new Exception("Zero resulting HOENTs after sentence: [" + this.toString() + "]");
                    //}

                    //newStructures.add(newStructure);
                    //leftConditions = leftConditions && structure.hCheckCompatibility(posEvaluate, timeID);
                }
                newStructures.add(structure.copy());
            }
        }
        else {
            //empty if part=================================================================================================
            //A causes a if p

            for (Hoent structure : structures) {
                boolean isAtLeastOneNewStructure = false;
                //boolean addedIdenticalStructure = false;

                boolean leftConditions = true;

                leftConditions = leftConditions && structure.eIsActionAtTime(this.action.actionID, timeID);
                if (leftConditions == false) {
                    //newStructures.add(structure.copy());
                    continue;
                }

                Hoent newStructure = structure.copy();
                //newStructure.hAddNewEvaluates(newEvaluates, timeID); //ifCondition

                //resultCondition
                boolean isAtLeastOneSameResultingStructure = false;
                boolean isAtLeastOneResultingStructure = false;
                ArrayList<ArrayList<String>> posAndNegEvaluatesOfResultCondition =
                        FormulaUtils.getPositiveAndNegativeEvaluates(this.causesFormula, fluentsCount);
                ArrayList<String> posEvaluatesOfResultCondition = posAndNegEvaluatesOfResultCondition.get(0); //e.g., ?100? [fluentIDs: 2,3,4; negations: 0,1,1; fluentCount: 5]
                for (String posEvaluateOfResultCondition : posEvaluatesOfResultCondition) {
                    boolean hCompatibilityOfResCond = newStructure.hCheckCompatibility(posEvaluateOfResultCondition, (byte) (timeID + 1));
                    if (hCompatibilityOfResCond == false) {
                        continue;
                    }
                    String newEvaluatesOfResultCondition = newStructure.hGetNewEvaluates(posEvaluateOfResultCondition,
                            (byte) (timeID + 1));
                    byte zerosAndOnesCounterOfResultCondition = StringUtils.countZerosAndOnes(newEvaluatesOfResultCondition);
                    if (zerosAndOnesCounterOfResultCondition == 0) {
                        if (isAtLeastOneSameResultingStructure == false) {
                            Hoent newStructureOfResultCondition = newStructure.copy();
                            newStructureOfResultCondition.oAddFluents(this.causesFormula.getFluentsIDs(), (byte) (timeID + 1));
                            newStructures.add(newStructureOfResultCondition);
                        }
                        isAtLeastOneSameResultingStructure = true;
                        isAtLeastOneResultingStructure = true;
                        continue;
                    }
                    Hoent newStructureOfResultCondition = newStructure.copy();
                    newStructureOfResultCondition.hAddNewEvaluates(newEvaluatesOfResultCondition, (byte) (timeID + 1));
                    newStructureOfResultCondition.oAddFluents(this.causesFormula.getFluentsIDs(), (byte) (timeID + 1));
                    newStructures.add(newStructureOfResultCondition);
                    isAtLeastOneResultingStructure = true;
                }
                //if (isAtLeastOneResultingStructure == false) {
                //    throw new Exception("Zero resulting HOENTs after sentence: [" + this.toString() + "]");
                //}

                //newStructures.add(newStructure);
                //leftConditions = leftConditions && structure.hCheckCompatibility(posEvaluate, timeID);

                newStructures.add(structure.copy()); 
            }
        }

        return newStructures;
    }
}
