package pl.edu.pw.mini.msi.knowledgerepresentation.data;

import java.util.List;

/**
 * Created by rwyka on 5/10/15.
 */
public class Scenario {
    public final List<ScenarioACSPart> ACS;
    public final List<ScenarioOBSPart> OBS;

    public Scenario(List<ScenarioACSPart> ACS, List<ScenarioOBSPart> OBS) {
        this.ACS = ACS;
        this.OBS = OBS;
    }

    @Override
    public String toString() {
        return "Scenario{" +
                "ACS=" + ACS +
                ", OBS=" + OBS +
                '}';
    }
}
