package pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentences;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.ActionDomain;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts.Action;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts.Fluent;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts.FormulaUtils;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts.IFormula;
import pl.edu.pw.mini.msi.knowledgerepresentation.hoents.Hoent;
import pl.edu.pw.mini.msi.knowledgerepresentation.hoents.HoentsSettings;
import pl.edu.pw.mini.msi.knowledgerepresentation.utils.StringUtils;

import java.util.ArrayList;

/**
 * Created by Tomek on 2015-08-29.
 */
public class ReleasesSentence extends Sentence {

    private static final Logger log = LoggerFactory.getLogger(ReleasesSentence.class);

    public Action action;
    public Fluent fluent;
    public IFormula conditionFormula;

    public ReleasesSentence(Action action, Fluent fluent, IFormula conditionFormula, ActionDomain actionDomain){
        this.action = action;
        this.fluent = fluent;
        this.conditionFormula = conditionFormula;

        super.addFluentToActionDomain(fluent.fluent, actionDomain);
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
        fluent.fillFluentID(fluentMappings);
        action.fillFluentsIDs(actionMappings);
        if (conditionFormula != null) {
            conditionFormula.fillFluentsIDs(fluentMappings);
        }
    }

    @Override
    public String toString() {
        return "[" + action.toString() + " releases " + fluent.toString() +
                " if [" + StringUtils.TSIN(conditionFormula) + "]]";
    }

    @Override
    public ArrayList<Hoent> applyCertainSentence(ArrayList<Hoent> structures, byte fluentsCount, byte timeID,
                                                 boolean secondPass, HoentsSettings hoentsSettings)
            throws Exception{
        //A releases f if p
        ArrayList<Hoent> newStructures = new ArrayList<Hoent>();

        if (this.conditionFormula != null) {
            ArrayList<ArrayList<String>> posAndNegEvaluates =
                    FormulaUtils.getPositiveAndNegativeEvaluates(this.conditionFormula, fluentsCount);
            ArrayList<String> posEvaluates = posAndNegEvaluates.get(0); //e.g., ?100? [fluentIDs: 2,3,4; negations: 0,1,1; fluentCount: 5]
            ArrayList<String> negEvaluates = posAndNegEvaluates.get(1);

            for (Hoent structure : structures) {
                boolean isAtLeastOneNewStructure = false;
                //boolean addedIdenticalStructure = false;

                //newStructures.add(structure.copy()); //20150905

                if (structure.eIsActionAtTime(this.action.actionID, timeID) == false) { //20150909 moved from the inside of "for (String posEvaluate : posEvaluates) {"
                    newStructures.add(structure.copy()); //20150905
                    continue;
                }

                //if (posEvaluates.size() == 0) { //20150909 //20150911
                //    newStructures.add(structure.copy()); //20150905
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

                    boolean hCompatibility = structure.hCheckCompatibility(posEvaluate, timeID);
                    if (hCompatibility == false) {
                        //newStructures.add(structure.copy()); //20150905 //20150911
                        continue;
                    }
                    String newEvaluates = structure.hGetNewEvaluates(posEvaluate, timeID);
                    byte zerosAndOnesCounter = StringUtils.countZerosAndOnes(newEvaluates);
                    //if (zerosAndOnesCounter == 0) {
                    //    //newStructures.add(structure.copy());
                    //    continue;
                    //}
                    if (zerosAndOnesCounter != 0) {
                        //newStructures.add(structure.copy()); //add structure with "?'s" //20150911_add?
                    }
                    Hoent newStructure = structure.copy();
                    newStructure.hAddNewEvaluates(newEvaluates, timeID); //ifCondition
                    ArrayList<Byte> oneFLuentAL = new ArrayList<Byte>();
                    oneFLuentAL.add(this.fluent.fluentID);
                    newStructure.oAddFluents(this.action.actionID, oneFLuentAL, timeID);

                    newStructures.add(newStructure);
                    //leftConditions = leftConditions && structure.hCheckCompatibility(posEvaluate, timeID);
                }
            }
        }
        else {
            //empty if part=================================================================================================
            //A releases f if p

            for (Hoent structure : structures) {
                boolean isAtLeastOneNewStructure = false;
                //boolean addedIdenticalStructure = false;

                if (structure.eIsActionAtTime(this.action.actionID, timeID) == false) {
                    newStructures.add(structure.copy());
                    continue;
                }

                Hoent newStructure = structure.copy();
                //newStructure.hAddNewEvaluates(newEvaluates, timeID); //ifCondition
                ArrayList<Byte> oneFLuentAL = new ArrayList<Byte>();
                oneFLuentAL.add(this.fluent.fluentID);
                newStructure.oAddFluents(this.action.actionID, oneFLuentAL, timeID);

                newStructures.add(newStructure);

                //newStructures.add(structure.copy()); //TODO comment this?
            }
        }

        if (newStructures.size() == 0) {
            throw new Exception("Zero HOENTs (contradictory action domain) after sentence: [" + this.toString() + "]");
        }

        return newStructures;
    }
}
