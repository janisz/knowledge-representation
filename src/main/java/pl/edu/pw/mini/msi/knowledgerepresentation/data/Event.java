package pl.edu.pw.mini.msi.knowledgerepresentation.data;

public class Event {
    Action action;
    Time time;

    @java.beans.ConstructorProperties({"action", "time"})
    public Event(Action action, Time time) {
        this.action = action;
        this.time = time;
    }

    public Action getAction() {
        return this.action;
    }

    public int getTime() {
        return this.time.getTime();
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Event)) return false;
        final Event other = (Event) o;
        final Object this$action = this.action;
        final Object other$action = other.action;
        if (this$action == null ? other$action != null : !this$action.equals(other$action)) return false;
        final Object this$time = this.time;
        final Object other$time = other.time;
        if (this$time == null ? other$time != null : !this$time.equals(other$time)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $action = this.action;
        result = result * PRIME + ($action == null ? 0 : $action.hashCode());
        final Object $time = this.time;
        result = result * PRIME + ($time == null ? 0 : $time.hashCode());
        return result;
    }

    public String toString() {
       return "Event(action=" + this.action + ", time=" + this.time + ")";
    }
}
