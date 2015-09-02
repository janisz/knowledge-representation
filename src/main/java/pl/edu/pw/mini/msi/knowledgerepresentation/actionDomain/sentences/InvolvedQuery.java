package pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentences;

import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts.Actor;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts.EQueryType;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts.Scenario;
import pl.edu.pw.mini.msi.knowledgerepresentation.utils.StringUtils;

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
}
