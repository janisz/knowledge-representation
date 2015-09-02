package pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentences;

import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts.*;
import pl.edu.pw.mini.msi.knowledgerepresentation.utils.StringUtils;

/**
 * Created by Tomek on 2015-08-30.
 */
public class PerformedQuery extends Query {
    //a/e/t performed A at t when sc
    public EQueryType queryType;
    public Action action; //Action, not Task
    public Time time;
    public Scenario scenario;

    public PerformedQuery(EQueryType queryType, Action action, Time time, Scenario scenario) {
        super();
        this.queryType = queryType;
        this.action = action;
        this.time = time;
        this.scenario = scenario;
    }

    @Override
    public String getScenarioName() {
        return scenario.scenario;
    }

    @Override
    public String toString() {
        return "[" + queryType.toString() + " performed " + action.toString() + " at " + time.toString() +
                " when " + scenario.toString() + "]";
    }
}
