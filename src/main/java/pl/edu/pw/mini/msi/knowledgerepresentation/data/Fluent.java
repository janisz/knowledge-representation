package pl.edu.pw.mini.msi.knowledgerepresentation.data;

public class Fluent implements Cloneable {
    String name;
    boolean positive;

    @java.beans.ConstructorProperties({"name", "positive"})
    public Fluent(String name, boolean positive) {
        this.name = name;
        this.positive = positive;
    }

    public Fluent not() {
        return new Fluent(name, !positive);
    }

    public String getName() {
        return this.name;
    }

    public boolean isPositive() {
        return positive;
    }

    public boolean sameName(Fluent fluent) {
        return name.equals(fluent.getName());
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Fluent)) return false;
        final Fluent other = (Fluent) o;
        final Object this$name = this.name;
        final Object other$name = other.name;
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        if (this.positive != other.positive) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $name = this.name;
        result = result * PRIME + ($name == null ? 0 : $name.hashCode());
        result = ((result * PRIME) + (this.positive ? 79 : 97));
        return result;
    }

    public String toString() {
       return "Fluent(name=" + this.name + ", positive=" + this.positive + ")";
    }

    @Override
    public Fluent clone() {
        return new Fluent(name, positive);
    }
}
