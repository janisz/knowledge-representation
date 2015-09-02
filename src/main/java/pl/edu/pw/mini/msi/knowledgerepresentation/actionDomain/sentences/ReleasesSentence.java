package pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentences;

import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.ActionDomain;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts.Action;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts.Fluent;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts.IFormula;
import pl.edu.pw.mini.msi.knowledgerepresentation.utils.StringUtils;

import java.util.ArrayList;

/**
 * Created by Tomek on 2015-08-29.
 */
public class ReleasesSentence extends Sentence {
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
    }

    @Override
    public String toString() {
        return "[" + action.toString() + " releases " + fluent.toString() +
                " if [" + StringUtils.TSIN(conditionFormula) + "]]";
    }
}
