package pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentences;

import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts.*;
import pl.edu.pw.mini.msi.knowledgerepresentation.hoents.Hoent;
import pl.edu.pw.mini.msi.knowledgerepresentation.utils.ArrayListOfStringUtils;
import pl.edu.pw.mini.msi.knowledgerepresentation.utils.StringUtils;

import java.util.ArrayList;

/**
 * Created by Tomek on 2015-08-30.
 */
public class PerformedQuery extends Query {
    //a/e/t performed A at t when sc
    public EQueryType queryType;
    public Action action; //Action, not Task
    public Time time;
    public ScenarioName scenarioName;

    public PerformedQuery(EQueryType queryType, Action action, Time time, ScenarioName scenarioName) {
        super();
        this.queryType = queryType;
        this.action = action;
        this.time = time;
        this.scenarioName = scenarioName;
    }

    @Override
    public String getScenarioName() {
        return scenarioName.scenarioName;
    }

    @Override
    public String toString() {
        return "[" + queryType.toString() + " performed " + action.toString() + " at " + time.toString() +
                " when " + scenarioName.toString() + "]";
    }

    @Override
    public void fillFluentAndActionIDs(ArrayList<String> fluentMappings, ArrayList<String> actionMappings) {
        this.action.fillFluentsIDs(actionMappings);
    }

    @Override
    public Boolean getAnswer(ArrayList<Hoent> modelsOfTypeOne, ArrayList<Hoent> modelsOfTypeTwo, ArrayList<String> actions) {
        byte actionID = ArrayListOfStringUtils.getIndexOfString(actions, this.action.toString());

        if (queryType == EQueryType.always) {
            for (Hoent model : modelsOfTypeOne) {
                boolean result = model.eIsActionAtTime(actionID, this.time.timeID);
                if (result == false) {
                    return false;
                }
                else {
                    ; //empty
                }
            }
            return true;
        }
        else if (queryType == EQueryType.ever) {
            for (Hoent model : modelsOfTypeOne) {
                boolean result = model.eIsActionAtTime(actionID, this.time.timeID);
                if (result == true) {
                    return true;
                }
                else {
                    ; //empty
                }
            }
            return false;
        }
        else {//if (queryType == EQueryType.typically) {
            for (Hoent model : modelsOfTypeTwo) {
                boolean result = model.eIsActionAtTime(actionID, this.time.timeID);
                if (result == false) {
                    return false;
                }
                else {
                    ; //empty
                }
            }
            return true;
        }
    }
}
