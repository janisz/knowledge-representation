package pl.edu.pw.mini.msi.knowledgerepresentation.data;

import com.google.common.collect.Multimap;
import lombok.Value;

import java.util.Map;

@Value
public class Scenario {
    String name;
    Multimap<Time, Fluent> observations;
    Map<Time, Action> actions;
}
