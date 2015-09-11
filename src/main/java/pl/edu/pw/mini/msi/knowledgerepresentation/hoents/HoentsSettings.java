package pl.edu.pw.mini.msi.knowledgerepresentation.hoents;

/**
 * Created by Tomek on 2015-09-11.
 */
public class HoentsSettings {
    private boolean doThrow;
    private boolean doThrowIfExceededTimeLimit;

    public HoentsSettings(boolean doThrow, boolean doThrowIfExceededTimeLimit) {
        this.doThrow = doThrow;
        this.doThrowIfExceededTimeLimit = doThrowIfExceededTimeLimit;
    }

    public HoentsSettings() {
        //defaults
        this.doThrow = true;
        this.doThrowIfExceededTimeLimit = false;
    }

    public boolean isDoThrow() {
        return this.doThrow;
    }

    public boolean isDoThrowIfExceededTimeLimit() {
        return this.doThrowIfExceededTimeLimit;
    }
}
