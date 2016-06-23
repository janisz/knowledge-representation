package pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentences;

import pl.edu.pw.mini.msi.knowledgerepresentation.hoents.Hoent;

import java.util.ArrayList;

/**
 * Created by Tomek on 2015-08-30.
 */
public abstract class Query {
    public static int counter = 0;

    public int id;

    public Query(){
        id = Query.counter;
        Query.counter++;
    }

    public abstract String getScenarioName();

    public abstract Boolean getAnswer(ArrayList<Hoent> modelsOfTypeOne, ArrayList<Hoent> modelsOfTypeTwo,
                                      ArrayList<String> actions);
    public abstract void fillFluentAndActionIDs(ArrayList<String> fluentMappings, ArrayList<String> actionMappings);
}
