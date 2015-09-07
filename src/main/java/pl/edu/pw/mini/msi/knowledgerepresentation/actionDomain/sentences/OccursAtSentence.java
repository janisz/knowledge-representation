package pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentences;

import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.ActionDomain;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts.Action;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts.Time;
import pl.edu.pw.mini.msi.knowledgerepresentation.hoents.Hoent;
import pl.edu.pw.mini.msi.knowledgerepresentation.hoents.SysElemEAtTimeUnit;
import pl.edu.pw.mini.msi.knowledgerepresentation.utils.ArrayListOfByteUtils;
import pl.edu.pw.mini.msi.knowledgerepresentation.utils.StringUtils;

import java.util.ArrayList;

/**
 * Created by Tomek on 2015-08-29.
 */
public class OccursAtSentence extends Sentence {
    public boolean typically;
    public Action action;
    public Time time;

    public OccursAtSentence(boolean typically, Action action, Time time, ActionDomain actionDomain) {
        this.typically = typically;
        this.action = action;
        this.time = time;

        super.addActionToActionDomain(action, actionDomain);
    }

    @Override
    public boolean isTypical() {
        return typically;
    }

    @Override
    public void fillFluentAndActionIDs(ArrayList<String> fluentMappings, ArrayList<String> actionMappings) {
        action.fillFluentsIDs(actionMappings);
    }

    @Override
    public String toString() {
        String typicallyString = StringUtils.booleanTypicallyToString(typically);
        return "[" + typicallyString + action.toString() + " occurs at " + time.toString() + "]";
    }

    @Override
    public ArrayList<Hoent> applyCertainSentence(ArrayList<Hoent> structures, byte fluentsCount, byte timeIDDoNotUse)
            throws Exception {
        //A occurs at t
        byte time = this.time.timeID;

        for (Hoent structure : structures) {
            SysElemEAtTimeUnit eAtTime = structure.sysElemE.get(time);

            if (this.action.task.negated) {
                ArrayListOfByteUtils.insertIntoArrayList(eAtTime.disallowedActions, this.action.actionID);
                continue;
            }

            //negated == false
            if (eAtTime.occuringAction != -1) {
                throw new Exception("Conflicting actions while processing sentence [" + this.toString() + "]");
            }

            eAtTime.occuringAction = this.action.actionID;

        }

        return structures;
    }

    @Override
    public ArrayList<Hoent> applyTypicalSentence(ArrayList<Hoent> structures, byte fluentsCount, byte timeIDDoNotUse,
                                                 boolean secondPass)
            throws Exception {
        byte time = this.time.timeID;
        ArrayList<Hoent> newStructures = new ArrayList<Hoent>();

        for (Hoent structure : structures) {
            SysElemEAtTimeUnit eAtTime = structure.sysElemE.get(time);

            //change compared to applyCertainSentence
            newStructures.add(structure.copy()); //not typically, action doesn't occur
            if (secondPass) { //20150906
                continue;
            }

            if (eAtTime.occuringAction != -1) {
                //change compared to applyCertainSentence
                continue;
            }

            //change compared to applyCertainSentence
            Hoent newStructure = structure.copy();
            newStructure.nSetToTrue(time, this.action.actionID);
            newStructure.eAddAction(this.action.actionID, time); //20150906_2
            newStructures.add(newStructure);

        }


        return newStructures;
    }
}
