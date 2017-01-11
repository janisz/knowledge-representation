package pl.edu.pw.mini.msi.knowledgerepresentation.hoents;

import com.google.common.base.Joiner;
import pl.edu.pw.mini.msi.knowledgerepresentation.utils.ArrayListOfByteUtils;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Created by Tomek on 2015-08-31.
 */
public class SysElemEAtTimeUnit {
    public byte occuringAction;
    public ArrayList<Byte> disallowedActions = new ArrayList<Byte>();
    //boolean isOccuringActionTriggeredByQuestionMark;

    public SysElemEAtTimeUnit() {
        this.occuringAction = -1;
        this.disallowedActions = new ArrayList<Byte>();
        //this.isOccuringActionTriggeredByQuestionMark = false;
    }

    public SysElemEAtTimeUnit copy() {
        SysElemEAtTimeUnit newSysElemEAtTimeUnit = new SysElemEAtTimeUnit();

        newSysElemEAtTimeUnit.occuringAction = this.occuringAction;
        newSysElemEAtTimeUnit.disallowedActions.addAll(this.disallowedActions.stream().map(Byte::new).collect(Collectors.toList()));
        //newSysElemEAtTimeUnit.isOccuringActionTriggeredByQuestionMark = this.isOccuringActionTriggeredByQuestionMark;

        return newSysElemEAtTimeUnit;
    }

    @Override
    public String toString() {
        String result = "Occurring: " + this.occuringAction + ", disallowed: " +
                Joiner.on(", ").useForNull("null").join(this.disallowedActions);
        return result;
    }

    public boolean areSame(SysElemEAtTimeUnit otherE) {
        if (this.occuringAction != otherE.occuringAction) {
            return false;
        }
        if (this.disallowedActions.size() != otherE.disallowedActions.size()) {
            return false;
        }
        for (byte thisDisallowedAction : this.disallowedActions) {
            if (ArrayListOfByteUtils.contains(otherE.disallowedActions, thisDisallowedAction) == false) {
                return false;
            }
        }
        return true;
    }
}
