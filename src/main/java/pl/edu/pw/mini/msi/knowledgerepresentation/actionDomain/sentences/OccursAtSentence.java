package pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentences;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.ActionDomain;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts.Action;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts.Time;
import pl.edu.pw.mini.msi.knowledgerepresentation.hoents.Hoent;
import pl.edu.pw.mini.msi.knowledgerepresentation.hoents.HoentsSettings;
import pl.edu.pw.mini.msi.knowledgerepresentation.hoents.SysElemEAtTimeUnit;
import pl.edu.pw.mini.msi.knowledgerepresentation.utils.ArrayListOfByteUtils;
import pl.edu.pw.mini.msi.knowledgerepresentation.utils.StringUtils;

import java.util.ArrayList;

/**
 * Created by Tomek on 2015-08-29.
 */
public class OccursAtSentence extends Sentence {

    private static final Logger log = LoggerFactory.getLogger(OccursAtSentence.class);

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
    public ArrayList<Hoent> applyCertainSentence(ArrayList<Hoent> structures, byte fluentsCount, byte timeIDDoNotUse,
                                                 boolean secondPass, HoentsSettings hoentsSettings)
            throws Exception {
        //A occurs at t
        byte time = this.time.timeID;
        //ArrayList<Hoent> newHoents = new ArrayList<Hoent>();

        ArrayList<Hoent> newStructures = new ArrayList<Hoent>();

        for (Hoent structure : structures) {
            SysElemEAtTimeUnit eAtTime = structure.sysElemE.get(time);

            if (this.action.task.negated) {
                if (!structure.eCanAddNegatedActionAtTime(this.action.actionID, time)) {
                    String message = "Conflicting actions while processing sentence [" + this.toString() + "] secondPass==[" + secondPass + "].";
                    if (hoentsSettings.isDoThrow() && !structure.isActionTypicalAtTime(time)) {
                        throw new Exception(message);
                    }
                    else {
                        continue; //not relevant
                    }
                }
                ArrayListOfByteUtils.insertIntoArrayList(eAtTime.disallowedActions, this.action.actionID);
                newStructures.add(structure.copy());
                continue;
            }

            //negated == false
            if (!structure.eCanInsertActionAtTime(this.action.actionID, time)) { //20150909
            //if (eAtTime.occuringAction != -1) {
                String message = "Conflicting actions while processing sentence [" + this.toString() + "] secondPass==[" + secondPass + "].";
                if (hoentsSettings.isDoThrow() && !structure.isActionTypicalAtTime(time)) {
                    throw new Exception(message);
                }
                else {
                    continue; //not relevant
                }
            }

            eAtTime.occuringAction = this.action.actionID;
            newStructures.add(structure.copy());
        }
        return newStructures;
    }

    @Override
    public ArrayList<Hoent> applyTypicalSentence(ArrayList<Hoent> structures, byte fluentsCount, byte timeIDDoNotUse,
                                                 boolean secondPass, HoentsSettings hoentsSettings)
            throws Exception {
        byte time = this.time.timeID;
        ArrayList<Hoent> newStructures = new ArrayList<Hoent>();

        for (Hoent structure : structures) {
            structure.addTypicalActionIndex(this.time.timeID);

            if (secondPass) { //20150906
                continue;
            }

            if (!structure.eIsActionAtTime(this.action.actionID, time)) { //20150913
                Hoent newAStructure = structure.copy();
                newAStructure.aAddAtypicalAction(time, this.action.actionID);
                newStructures.add(newAStructure); //not typically, action doesn't occur
            }

            //negated == false
            if (!structure.eCanInsertActionAtTime(this.action.actionID, time)) { //20150909
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
