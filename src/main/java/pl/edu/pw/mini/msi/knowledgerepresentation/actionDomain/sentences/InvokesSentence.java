package pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentences;

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
            this.time = new Time("0");
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
        conditionFormula.fillFluentsIDs(fluentMappings);
        causalAction.fillFluentsIDs(actionMappings);
        resultingAction.fillFluentsIDs(actionMappings);
    }

    @Override
    public String toString() {
        String typicallyString = StringUtils.booleanTypicallyToString(typically);
        return "[" + typicallyString + causalAction.toString() + " invokes " + resultingAction.toString() +
                "after " + time.toString() + " if [" + StringUtils.TSIN(conditionFormula) + "]]";
    }

    /*public ArrayList<Hoent> applyCertainSentence(ArrayList<Hoent> structures, byte fluentsCount, byte timeID)
            throws Exception {
        //A invokes B after t if p
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
    }*/
}
