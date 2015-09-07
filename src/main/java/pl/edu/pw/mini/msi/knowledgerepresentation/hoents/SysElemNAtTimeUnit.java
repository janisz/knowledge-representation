package pl.edu.pw.mini.msi.knowledgerepresentation.hoents;

import java.util.Objects;

/**
 * Created by Tomek on 2015-09-03.
 */
public class SysElemNAtTimeUnit {
    public Byte actionID;

    public SysElemNAtTimeUnit() {
        this.actionID = -1;
    }

    public SysElemNAtTimeUnit copy(){
        SysElemNAtTimeUnit newN = new SysElemNAtTimeUnit();
        newN.actionID = this.actionID;
        return newN;
    }

    public boolean areSame(SysElemNAtTimeUnit otherN) {
        return Objects.equals(this.actionID, otherN.actionID);
    }

    @Override
    public String toString() {
        return "ActionID = " + actionID.toString();
    }
}
