package pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentences;

import pl.edu.pw.mini.msi.knowledgerepresentation.hoents.Hoent;

import java.util.ArrayList;

public class CommonMethod {
    private byte timeID;
    private ArrayList<Hoent> newStructures;
    private ArrayList<String> negEvaluates;
    private Hoent structure;

    public CommonMethod(byte timeID, ArrayList<Hoent> newStructures, ArrayList<String> negEvaluates, Hoent structure) {
        this.timeID = timeID;
        this.newStructures = newStructures;
        this.negEvaluates = negEvaluates;
        this.structure = structure;
    }

    public void invoke() {
        for (String negEvaluate : negEvaluates) { //20150911

            boolean hCompatibility = structure.hCheckCompatibility(negEvaluate, timeID);
            if (hCompatibility == false) {
                //newStructures.add(structure.copy());
                continue;
            }
            String newEvaluates = structure.hGetNewEvaluates(negEvaluate, timeID);
            Hoent newStructure = structure.copy();
            newStructure.hAddNewEvaluates(newEvaluates, timeID); //ifCondition
            newStructures.add(newStructure);
        }
    }
}