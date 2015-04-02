package pl.edu.pw.mini.msi.knowledgerepresentation;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import pl.edu.pw.mini.msi.knowledgerepresentation.data.Actor;
import pl.edu.pw.mini.msi.knowledgerepresentation.data.Fluent;
import pl.edu.pw.mini.msi.knowledgerepresentation.data.Identifier;
import pl.edu.pw.mini.msi.knowledgerepresentation.data.Scenario;

import java.util.Collection;
import java.util.HashMap;

public class Context {
    Collection<Actor> actors = Sets.newLinkedHashSet();
    Collection<Fluent> fluents = Sets.newLinkedHashSet();
    Collection<Identifier> identifiers = Sets.newLinkedHashSet();
    HashMap<Identifier, Scenario> scenarios = Maps.newLinkedHashMap();
}
