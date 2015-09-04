package pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentences;

import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts.EQueryType;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts.IFormula;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts.ScenarioName;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts.Time;
import pl.edu.pw.mini.msi.knowledgerepresentation.hoents.Hoent;

import java.util.ArrayList;

/**
 * Created by Tomek on 2015-08-30.
 */
public class AtQuery extends Query {
    //a/e/t gamma at t when sc
    public EQueryType queryType;
    public IFormula formula;
    public Time time;
    public ScenarioName scenarioName;

    public AtQuery(EQueryType queryType, IFormula formula, Time time, ScenarioName scenarioName) {
        super();
        this.queryType = queryType;
        this.formula = formula;
        this.time = time;
        this.scenarioName = scenarioName;
    }

    @Override
    public String getScenarioName() {
        return scenarioName.scenarioName;
    }

    @Override
    public String toString() {
        return "[" + queryType + formula.toString() + " at " + time.toString() + " when " + scenarioName.toString() + "]";
    }

    @Override
    public Boolean getAnswer(ArrayList<Hoent> modelsOfTypeOne, ArrayList<Hoent> modelsOfTypeTwo, ArrayList<String> actions) {
        if (queryType == EQueryType.always) {
            for (Hoent model : modelsOfTypeOne) {
                char result = this.formula.evaluateForValues( model.sysElemH.get( this.time.timeID ) );
                if (result == '0') {
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
                char result = this.formula.evaluateForValues( model.sysElemH.get( this.time.timeID ) );
                if (result == '1') {
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
                char result = this.formula.evaluateForValues( model.sysElemH.get( this.time.timeID ) );
                if (result == '0') {
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
