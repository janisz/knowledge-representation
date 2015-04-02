package pl.edu.pw.mini.msi.knowledgerepresentation.data;

import com.google.common.collect.Maps;
import lombok.Value;

import java.util.Collection;
import java.util.Map;

@Value
public class Scenario {
    String name;
    Map<Time, Collection<Fluent>> observations = Maps.newHashMap();
    Map<Time, Fluent> actions = Maps.newHashMap();
}
