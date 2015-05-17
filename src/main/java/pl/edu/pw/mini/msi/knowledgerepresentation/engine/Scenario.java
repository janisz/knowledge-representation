package pl.edu.pw.mini.msi.knowledgerepresentation.engine;

import pl.edu.pw.mini.msi.knowledgerepresentation.data.Action;
import pl.edu.pw.mini.msi.knowledgerepresentation.data.Fluent;

import java.util.ArrayList;
import java.util.List;


public class Scenario {
    public List<ScenarioACSPart> ACS = new ArrayList<ScenarioACSPart>();
    public List<ScenarioOBSPart> OBS = new ArrayList<ScenarioOBSPart>();

    public Scenario() {
        Action a = new Action("Janek", "takesCard");
        Action b = new Action("Janek", "locksTheDoor");
        Action c = new Action("Janek", "comeback");
        Action d = new Action("Janek", "leaves");

        ACS.add(new ScenarioACSPart(a, 1));
        ACS.add(new ScenarioACSPart(d, 2));
        ACS.add(new ScenarioACSPart(b, 3));
        ACS.add(new ScenarioACSPart(c, 10));

        FluentList fa = new FluentList();
        fa.add(new Fluent("hasCard", true));
        fa.add(new Fluent("inHostel", true));

        FluentList fb = new FluentList();
        fb.add(new Fluent("hasCard", false));

        OBS.add(new ScenarioOBSPart(fa, 4));
        OBS.add(new ScenarioOBSPart(fb, 10));
    }
}
