package pl.edu.pw.mini.msi.knowledgerepresentation.hoents;

import com.google.common.base.Joiner;

import java.util.ArrayList;

/**
 * Created by Tomek on 2015-08-31.
 */
public class SysElemEAtTimeUnit {
    public byte occuringAction;
    public ArrayList<Byte> disallowedActions = new ArrayList<Byte>();

    public SysElemEAtTimeUnit() {
        this.occuringAction = -1;
        disallowedActions = new ArrayList<Byte>();
    }

    public SysElemEAtTimeUnit copy() {
        SysElemEAtTimeUnit newSysElemEAtTimeUnit = new SysElemEAtTimeUnit();

        newSysElemEAtTimeUnit.occuringAction = this.occuringAction;
        for (Byte by : this.disallowedActions) {
            newSysElemEAtTimeUnit.disallowedActions.add( new Byte(by) );
        }

        return newSysElemEAtTimeUnit;
    }

    @Override
    public String toString() {
        String result = "Occurring: " + this.occuringAction + ", disallowed: " +
                Joiner.on(", ").useForNull("null").join(this.disallowedActions);
        return result;
    }
}
