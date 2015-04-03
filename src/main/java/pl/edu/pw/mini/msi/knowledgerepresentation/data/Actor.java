package pl.edu.pw.mini.msi.knowledgerepresentation.data;

import lombok.Value;

@Value
public class Actor {
    public Actor(String actor) {
        this.name = actor;
    }

    String name;
}
