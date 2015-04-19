package pl.edu.pw.mini.msi.knowledgerepresentation.data;

import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.Theory;
import com.google.common.base.Throwables;
import lombok.Value;

@Value
public class Actor {
    String name;

    public Theory theory() {
        Theory theory = null;
        try {
            theory = new Theory(String.format("actor('%s').", name));
        } catch (InvalidTheoryException e) {
            Throwables.propagate(e);
        }
        return theory;
    }
}
