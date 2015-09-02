package pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentences;

import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.ActionDomain;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts.Action;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts.IFormula;
import pl.edu.pw.mini.msi.knowledgerepresentation.utils.StringUtils;

import java.util.ArrayList;

/**
 * Created by Tomek on 2015-08-29.
 */
public class TriggersSentence extends Sentence {
    public boolean typically;
    public IFormula conditionFormula;
    public Action action;

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
}
