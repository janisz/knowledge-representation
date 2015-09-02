package pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentences;

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
}
