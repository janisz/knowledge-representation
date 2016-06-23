package pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts;

/**
 * Created by Tomek on 2015-08-30.
 */
public class ScenarioName {
    public String scenarioName;

    public ScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
    }

    @Override
    public String toString() {
        return this.scenarioName;
    }
}
