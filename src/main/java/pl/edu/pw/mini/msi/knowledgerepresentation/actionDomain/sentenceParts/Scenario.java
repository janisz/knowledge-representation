package pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts;

/**
 * Created by Tomek on 2015-08-30.
 */
public class Scenario {
    public String scenario;

    public Scenario(String scenario) {
        this.scenario = scenario;
    }

    @Override
    public String toString() {
        return this.scenario;
    }
}
