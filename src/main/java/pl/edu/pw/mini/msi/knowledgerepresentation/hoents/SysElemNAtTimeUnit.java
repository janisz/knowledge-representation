package pl.edu.pw.mini.msi.knowledgerepresentation.hoents;

/**
 * Created by Tomek on 2015-09-03.
 */
public class SysElemNAtTimeUnit {
    public Byte actionID;
    //public Boolean isPresent;

    public SysElemNAtTimeUnit() {
        this.actionID = -1;
    }

    public SysElemNAtTimeUnit(byte actionID) {
        this.actionID = actionID;
    }

    public SysElemNAtTimeUnit copy(){
        SysElemNAtTimeUnit newN = new SysElemNAtTimeUnit();
        newN.actionID = this.actionID;
        return newN;
    }

    public boolean areSame(SysElemNAtTimeUnit otherN) {
        //20150915
        return this.actionID.equals(otherN.actionID);
    }

    @Override
    public String toString() {
        return "ActionID = " + actionID.toString();
    }
}
