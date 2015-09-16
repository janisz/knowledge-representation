package pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentences;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.ActionDomain;
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
public class AtSentence extends Sentence {

    private static final Logger log = LoggerFactory.getLogger(AtSentence.class);

    public IFormula formula;
    public Time time;

    public AtSentence(IFormula formula, Time time, ActionDomain actionDomain) {
        this.formula = formula;
        this.time = time;

        super.addFluentsToActionDomain(formula.getFluents(), actionDomain);
    }

    @Override
    public boolean isTypical() {
        return false;
    }

    @Override
    public void fillFluentAndActionIDs(ArrayList<String> fluentMappings, ArrayList<String> actionMappings) {
        formula.fillFluentsIDs(fluentMappings);
    }

    @Override
    public String toString() {
        return "[" + formula.toString() + "] at " + time.toString();
    }

    @Override
    public ArrayList<Hoent> applyCertainSentence(ArrayList<Hoent> structures, byte fluentsCount, byte timeIDDoNotUse,
                                                 boolean secondPass, HoentsSettings hoentsSettings)
            throws Exception {
        //a at t
        AtSentence atSentence = this;

        ArrayList<ArrayList<String>> posAndNegEvaluates =
                FormulaUtils.getPositiveAndNegativeEvaluates(atSentence.formula, fluentsCount);
        ArrayList<String> posEvaluates = posAndNegEvaluates.get(0); //e.g., ?100? [fluentIDs: 2,3,4; negations: 0,1,1; fluentCount: 5]

        byte timeID = this.time.timeID;

        ArrayList<Hoent> newStructures = new ArrayList<Hoent>();
        for (Hoent structure : structures) {
            boolean addedSameStructure = false;
            boolean isAtLEastOnePosEvalCompatible = false;
            for (String posEvaluate : posEvaluates) {
                if (!structure.hCheckCompatibility(posEvaluate, timeID)) {
                    continue;
                }
                isAtLEastOnePosEvalCompatible = true;
                String newEvaluates = structure.hGetNewEvaluates(posEvaluate, timeID);
                byte zerosAndOnesCounter = StringUtils.countZerosAndOnes(newEvaluates);
                if (zerosAndOnesCounter == 0) {
                    if (!addedSameStructure) {
                        newStructures.add(structure.copy());
                        addedSameStructure = true;
                    }
                    continue;
                }
                Hoent newStructure = structure.copy();
                newStructure.hAddNewEvaluates(newEvaluates, timeID);
                newStructures.add(newStructure);
            }
            if (!isAtLEastOnePosEvalCompatible) {
                String message = String.format("Error in applying sentence: [%s] - can't apply resulting condition at time [%s] secondPass==[%s].",
                        this.toString(), Byte.toString(timeID), secondPass);
                log.debug(message);
                if (hoentsSettings.isDoThrow() && !structure.isStateTypicalAtTime(timeID)) {
                    throw new Exception(message);
                }
            }
        }
        if (newStructures.size() == 0) {
            throw new Exception("Zero HOENTs (contradictory action domain) after sentence: [" + atSentence + "]");
        }
        return newStructures;
    }
}