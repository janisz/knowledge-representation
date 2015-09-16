package pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts;

/**
 * Created by Tomek on 2015-08-29.
 */
public class Time {
    public final byte timeID;
    public final String time;

    public Time(String time) {
        this.time = time;
        this.timeID = Byte.parseByte(time);
    }

    @Override
    public String toString() {
        return time;
    }
}
