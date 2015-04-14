package pl.edu.pw.mini.msi.knowledgerepresentation.data;

import lombok.Value;

@Value
public class Event {
    Action action;
    Time time;

}
