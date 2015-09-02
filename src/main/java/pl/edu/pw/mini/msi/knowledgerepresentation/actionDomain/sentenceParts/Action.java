package pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts;

import pl.edu.pw.mini.msi.knowledgerepresentation.utils.ArrayListOfStringUtils;

import java.util.ArrayList;

/**
 * Created by Tomek on 2015-08-29.
 */
public class Action {
    public Actor actor;
    public Task task;

    public byte actionID = -1;

    public void fillFluentsIDs(ArrayList<String> actionsMapping) {
        String actionString = this.toString();
        actionID = ArrayListOfStringUtils.getIndexOfString(actionsMapping, actionString);
    }

    @Override
    public String toString() {
        return "(" + actor.toString() + "," + task.toString() + ")";
    }

}