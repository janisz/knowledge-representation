package pl.edu.pw.mini.msi.knowledgerepresentation;

import pl.edu.pw.mini.msi.knowledgerepresentation.data.Action;
import pl.edu.pw.mini.msi.knowledgerepresentation.data.Fluent;
import pl.edu.pw.mini.msi.knowledgerepresentation.data.Time;

import java.util.Set;

interface System {

    boolean History(Set<Fluent> form, Time time);

    Set<Fluent> Oclusion(Action action, Time time);

    boolean Events(Action action, Time time);

    boolean NormalityRelation(Action action, Time time);

    boolean AbnormalityRelation(Action action, Time time);

    Time Time();
}
