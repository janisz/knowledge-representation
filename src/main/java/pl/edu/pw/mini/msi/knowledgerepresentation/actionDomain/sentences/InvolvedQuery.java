package pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentences;

import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts.Actor;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts.EQueryType;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts.Scenario;
import pl.edu.pw.mini.msi.knowledgerepresentation.hoents.Hoent;
import pl.edu.pw.mini.msi.knowledgerepresentation.utils.StringUtils;

import java.util.ArrayList;

/**
 * Created by Tomek on 2015-08-30.
 */
public class InvolvedQuery extends Query {
    //a/e involved Ag when Sc
    public EQueryType queryType;
    public Actor actor;
    public Scenario scenario;

    public InvolvedQuery(EQueryType queryType, Actor actor, Scenario scenario) {
        super();
        this.queryType = queryType;
        this.actor = actor;
        this.scenario = scenario;
    }

    @Override
    public String getScenarioName() {
        return scenario.scenario;
    }

    @Override
    public String toString() {
        return "[" + queryType + " involved " + actor.toString() + " when " + scenario.toString() + "]";
    }

    @Override
    public Boolean getAnswer(ArrayList<Hoent> modelsOfTypeOne, ArrayList<Hoent> modelsOfTypeTwo,
                             ArrayList<String> actionsMapping) {
        if (queryType == EQueryType.always) {
            for (Hoent model : modelsOfTypeOne) {
                boolean agentInvolvedInModel = false;
                for (byte timeIndex = 0; timeIndex < model.tMax; timeIndex++) {
                    boolean result = model.eIsAgentInvolvedAtTime(this.actor.actor, timeIndex, actionsMapping);
                    if (result == true) {
                        agentInvolvedInModel = true;
                        break;
                    }
                }
                if (agentInvolvedInModel == false) {
                    return false;
                }
            }
            return true;
        }
        else {//if (queryType == EQueryType.ever) {
            for (Hoent model : modelsOfTypeOne) {
                //boolean agentInvolvedInModel = false;
                for (byte timeIndex = 0; timeIndex < model.tMax; timeIndex++) {
                    boolean result = model.eIsAgentInvolvedAtTime(this.actor.actor, timeIndex, actionsMapping);
                    if (result == true) {
                        //agentInvolvedInModel = true;
                        return true;
                    }
                }
                //if (agentInvolvedInModel == false) {
                //    return false;
                //}
            }
            return false;
        }
    }
}
