package pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts;

import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentences.AtSentence;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentences.OccursAtSentence;

import java.util.ArrayList;

/**
 * Created by Tomek on 2015-09-04.
 */
public class Scenario {
    public String name;
    public ArrayList<AtSentence> formulas;
    public ArrayList<OccursAtSentence> actions;

    public Scenario() {
        name = null;
        formulas = new ArrayList<AtSentence>();
        actions = new ArrayList<OccursAtSentence>();
    }
}
