package pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentences;

import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.ActionDomain;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts.FormulaUtils;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts.IFormula;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts.Time;
import pl.edu.pw.mini.msi.knowledgerepresentation.hoents.Hoent;
import pl.edu.pw.mini.msi.knowledgerepresentation.utils.StringUtils;

import java.util.ArrayList;

/**
 * Created by Tomek on 2015-08-29.
 */
public class AtSentence extends Sentence {
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
    public ArrayList<Hoent> applyCertainSentence(ArrayList<Hoent> structures, byte fluentsCount, byte timeID)
            throws Exception {
        AtSentence atSentence = this;
        //String mask = atSentence.formula.getFluentsMask(fluentsCount);

        ArrayList<ArrayList<String>> posAndNegEvaluates =
                FormulaUtils.getPositiveAndNegativeEvaluates(atSentence.formula, fluentsCount);
        ArrayList<String> posEvaluates = posAndNegEvaluates.get(0); //e.g., ?100? [fluentIDs: 2,3,4; negations: 0,1,1; fluentCount: 5]
        //foreach H
        //      check compatibility, if not compatible - return error
        //foreach H
        //      clone if not already present in newSysElemH
//                for (String posEvaluate : posEvaluates) {
//                    for (Hoent hoent : structures) {
//                        boolean result = hoent.hCheckCompatibility(posEvaluate, time);
//                        if (!result) {
//                            throw new Exception("HOENT not compatible with sentence: [" + atSentence + "]");
//                        }
//                    }
//                }
        ArrayList<Hoent> newHoents = new ArrayList<Hoent>();
        for (String posEvaluate : posEvaluates) {
            for (Hoent structure : structures) {
                boolean result = structure.hCheckCompatibility(posEvaluate, timeID);
                if (result == false) {
                    continue;
                }
                String newEvaluates = structure.hGetNewEvaluates(posEvaluate, timeID);
                byte zerosAndOnesCounter = StringUtils.countZerosAndOnes(newEvaluates);
                if (zerosAndOnesCounter == 0) {
                    newHoents.add(structure.copy());
                    continue;
                }
                Hoent newHoent = structure.copy();
                newHoent.hAddNewEvaluates(newEvaluates, timeID);
                newHoents.add(newHoent);
            }
        }
        if (newHoents.size() == 0) {
            throw new Exception("Zero HOENTs (contradictory action domain) after sentence: [" + atSentence + "]");
        }
        //TODO remove duplicate HOENTs
        return newHoents;
    }

    @Override
    public ArrayList<Hoent> applyTypicalSentence(ArrayList<Hoent> structures, byte fluentsCount)
            throws Exception {
        return null;//empty
    }
}