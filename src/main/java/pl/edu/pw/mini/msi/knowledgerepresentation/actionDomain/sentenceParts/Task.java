package pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts;

import pl.edu.pw.mini.msi.knowledgerepresentation.utils.StringUtils;

/**
 * Created by Tomek on 2015-08-29.
 */
public class Task {
    public final boolean negated;
    public final String task;
    public final byte taskID = -1;

    public Task(boolean negated, String task) {
        this.negated = negated;
        this.task = task;
    }

    @Override
    public String toString() {
        return StringUtils.concatenateStringAndBoolean(task, negated);
    }

    public String toStringWithoutNegation() {
        return task;
    }

}
