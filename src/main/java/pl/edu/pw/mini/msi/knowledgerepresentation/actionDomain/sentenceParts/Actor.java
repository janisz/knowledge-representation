package pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts;

/**
 * Created by Tomek on 2015-08-29.
 */
public class Actor {
    public final String actor;

    public Actor(String actor) {
        this.actor = actor;
    }

    @Override
    public String toString() {
        return this.actor;
    }
}
