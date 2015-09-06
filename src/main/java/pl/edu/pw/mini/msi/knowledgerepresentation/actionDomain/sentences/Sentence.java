package pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentences;

import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.ActionDomain;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts.Action;
import pl.edu.pw.mini.msi.knowledgerepresentation.hoents.Hoent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tomek on 2015-08-29.
 */
public class Sentence {
    public void addFluentToActionDomain(String fluent, ActionDomain actionDomain) {
        if (actionDomain.fluents.size() == 0) {
            actionDomain.fluents.add(fluent);
        }
        for (int index = 0; index < actionDomain.fluents.size(); index++) {
            int comparisonResult = fluent.compareTo(actionDomain.fluents.get(index));
            if (comparisonResult == 0) {
                return;
            }
            if (comparisonResult < 0) {
                actionDomain.fluents.add(index, fluent);
                return;
            }
        }
        actionDomain.fluents.add(fluent); //append to end
        return;
    }

    public void addFluentsToActionDomain(List<String> fluents, ActionDomain actionDomain){
        for (String fluent : fluents) {
            this.addFluentToActionDomain(fluent, actionDomain);
        }
    }

    public void addActionToActionDomain(String action, ActionDomain actionDomain) {
        if (actionDomain.actions.size() == 0) {
            actionDomain.actions.add(action);
        }
        for (int index = 0; index < actionDomain.actions.size(); index++) {
            int comparisonResult = action.compareTo(actionDomain.actions.get(index));
            if (comparisonResult == 0) {
                return;
            }
            if (comparisonResult < 0) {
                actionDomain.actions.add(index, action);
                return;
            }
        }
        actionDomain.actions.add(action); //append to end
        return;
    }

    public void addActionToActionDomain(Action action, ActionDomain actionDomain){
        String actionString = "(" + action.actor.actor + "," + action.task.task + ")";
        addActionToActionDomain(actionString, actionDomain);
    }

    public boolean isTypical(){
        return false;
    }

    public void fillFluentAndActionIDs(ArrayList<String> fluentMappings, ArrayList<String> actionMappings) {
        ; //empty, should be overriden
        //throw new Exception("Sentence.fillFluentAndActionIDs(...) not overridden.");
    }

    public ArrayList<Hoent> applyCertainSentence(ArrayList<Hoent> structures, byte fluentsCount, byte timeID)
            throws Exception {
        //return null; //empty, should be overriden
        throw new Exception("Sentence.applyCertainSentence(...) not overridden.");
    }

    public ArrayList<Hoent> applyTypicalSentence(ArrayList<Hoent> structures, byte fluentsCount, byte timeID,
                                                 boolean secondPass)
            throws Exception {
        //return null; //empty, should be overriden
        throw new Exception("Sentence.applyTypicalSentence(...) not overridden.");
    }
}
