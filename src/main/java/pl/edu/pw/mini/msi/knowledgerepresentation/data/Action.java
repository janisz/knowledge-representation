package pl.edu.pw.mini.msi.knowledgerepresentation.data;

public class Action {
    Actor actor;
    Task task;

    @java.beans.ConstructorProperties({"actor", "task"})
    public Action(Actor actor, Task task) {
        this.actor = actor;
        this.task = task;
    }

    public Action(String actorName, String taskName) {
        this(new Actor(actorName), new Task(taskName));
    }

    public Actor getActor() {
        return this.actor;
    }

    public Task getTask() {
        return this.task;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Action)) return false;
        final Action other = (Action) o;
        final Object this$actor = this.actor;
        final Object other$actor = other.actor;
        if (this$actor == null ? other$actor != null : !this$actor.equals(other$actor)) return false;
        final Object this$task = this.task;
        final Object other$task = other.task;
        if (this$task == null ? other$task != null : !this$task.equals(other$task)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $actor = this.actor;
        result = result * PRIME + ($actor == null ? 0 : $actor.hashCode());
        final Object $task = this.task;
        result = result * PRIME + ($task == null ? 0 : $task.hashCode());
        return result;
    }

    public String toString() {
        return "Action(actor=" + this.actor + ", task=" + this.task + ")";
    }
}
