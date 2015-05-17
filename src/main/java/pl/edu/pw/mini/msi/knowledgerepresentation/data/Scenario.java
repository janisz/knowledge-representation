package pl.edu.pw.mini.msi.knowledgerepresentation.data;

import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.Theory;
import com.google.common.base.Throwables;
import com.google.common.collect.Multimap;

import java.util.Map;

public class Scenario {
    String name;
    Multimap<Time, Fluent> observations;
    Map<Time, Action> actions;

    @java.beans.ConstructorProperties({"name", "observations", "actions"})
    public Scenario(String name, Multimap<Time, Fluent> observations, Map<Time, Action> actions) {
        this.name = name;
        this.observations = observations;
        this.actions = actions;
    }

    public Theory theory() {
        //TODO: Save all information with scenario
        try {
            return new Theory(String.format("scenario('%s').", name));
        } catch (InvalidTheoryException e) {
            Throwables.propagate(e);
        }
        return null;
    }

    public String getName() {
        return this.name;
    }

    public Multimap<Time, Fluent> getObservations() {
        return this.observations;
    }

    public Map<Time, Action> getActions() {
        return this.actions;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Scenario)) return false;
        final Scenario other = (Scenario) o;
        final Object this$name = this.name;
        final Object other$name = other.name;
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        final Object this$observations = this.observations;
        final Object other$observations = other.observations;
        if (this$observations == null ? other$observations != null : !this$observations.equals(other$observations))
            return false;
        final Object this$actions = this.actions;
        final Object other$actions = other.actions;
        if (this$actions == null ? other$actions != null : !this$actions.equals(other$actions)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $name = this.name;
        result = result * PRIME + ($name == null ? 0 : $name.hashCode());
        final Object $observations = this.observations;
        result = result * PRIME + ($observations == null ? 0 : $observations.hashCode());
        final Object $actions = this.actions;
        result = result * PRIME + ($actions == null ? 0 : $actions.hashCode());
        return result;
    }

    public String toString() {
       return "Scenario(name=" + this.name + ", observations=" + this.observations + ", actions=" + this.actions + ")";
    }
}
