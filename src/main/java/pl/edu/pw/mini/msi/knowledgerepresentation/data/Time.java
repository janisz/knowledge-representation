package pl.edu.pw.mini.msi.knowledgerepresentation.data;

public class Time {
    int time;

    @java.beans.ConstructorProperties({"time"})
    public Time(int time) {
        this.time = time;
    }

    public int getTime() {
        return this.time;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Time)) return false;
        final Time other = (Time) o;
        if (this.time != other.time) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.time;
        return result;
    }

    public String toString() {
       return "Time(time=" + this.time + ")";
    }
}
