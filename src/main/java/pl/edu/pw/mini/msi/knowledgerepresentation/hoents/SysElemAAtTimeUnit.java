package pl.edu.pw.mini.msi.knowledgerepresentation.hoents;

import pl.edu.pw.mini.msi.knowledgerepresentation.utils.ArrayListOfByteUtils;

import java.util.ArrayList;

/**
 * Created by Tomek on 2015-09-10.
 */
public class SysElemAAtTimeUnit {
    public ArrayList<Byte> actionIDs;
    //public Boolean isPresent;

    public SysElemAAtTimeUnit() {
        this.actionIDs = new ArrayList<Byte>();
    }

    public SysElemAAtTimeUnit copy(){
        SysElemAAtTimeUnit newA = new SysElemAAtTimeUnit();
        newA.actionIDs = ArrayListOfByteUtils.copy(this.actionIDs);
        return newA;
    }

    public boolean areSame(SysElemAAtTimeUnit otherA) {
        return ArrayListOfByteUtils.areSame(this.actionIDs, otherA.actionIDs);
    }

    @Override
    public String toString() {
        return "ActionIDs = " + ArrayListOfByteUtils.myToString(actionIDs);
    }
}
