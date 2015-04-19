package pl.edu.pw.mini.msi.knowledgerepresentation.data;

import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.Theory;
import com.google.common.base.Throwables;
import lombok.Value;

@Value
public class Fluent {
    String name;
    boolean positive;

    public Fluent not() {
        return new Fluent(name, !positive);
    }

    public Theory theory() {
        Theory theory = null;
        try {
            theory = new Theory(String.format("fluent('%s').", name));
        } catch (InvalidTheoryException e) {
            Throwables.propagate(e);
        }
        return theory;
    }
}
