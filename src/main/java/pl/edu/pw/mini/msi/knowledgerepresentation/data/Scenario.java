package pl.edu.pw.mini.msi.knowledgerepresentation.data;

import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.Theory;
import com.google.common.base.Throwables;
import com.google.common.collect.Multimap;
import lombok.Value;

import java.util.Map;

@Value
public class Scenario {
    String name;
    Multimap<Time, Fluent> observations;
    Map<Time, Action> actions;

    public Theory theory() {
        //TODO: Save all information with scenario
        try {
            return new Theory(String.format("scenario('%s').", name));
        } catch (InvalidTheoryException e) {
            Throwables.propagate(e);
        }
        return null;
    }
}
