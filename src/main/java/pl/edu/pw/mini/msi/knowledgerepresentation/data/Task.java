package pl.edu.pw.mini.msi.knowledgerepresentation.data;

public class Task {
    String name;

    @java.beans.ConstructorProperties({"name"})
    public Task(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Task)) return false;
        final Task other = (Task) o;
        final Object this$name = this.name;
        final Object other$name = other.name;
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $name = this.name;
        result = result * PRIME + ($name == null ? 0 : $name.hashCode());
        return result;
    }

    public String toString() {
        return "Task(name=" + this.name + ")";
    }
}