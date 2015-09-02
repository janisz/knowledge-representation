package pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentences;

import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts.EQueryType;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts.IFormula;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts.Scenario;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts.Time;

/**
 * Created by Tomek on 2015-08-30.
 */
public class AtQuery extends Query {
    //a/e/t gamma at t when sc
    public EQueryType queryType;
    public IFormula formula;
    public Time time;
    public Scenario scenario;

    public AtQuery(EQueryType queryType, IFormula formula, Time time, Scenario scenario) {
        super();
        this.queryType = queryType;
        this.formula = formula;
        this.time = time;
        this.scenario = scenario;
    }

    @Override
    public String getScenarioName() {
        return scenario.scenario;
    }
}
