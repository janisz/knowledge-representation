package pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts.Action;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts.Actor;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts.Fluent;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts.Task;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentences.Query;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentences.Sentence;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Usage:
 *      1. create
 *      2. add baseSentences, scenarios, queries
 *      3. calculateFullScenarios()
 *      4. calculateMappedQueries()
 *      5. fillFluentsAndActionsIDs()
 *
 * Created by Tomek on 2015-08-29.
 */
public class ActionDomain {

    private static final Logger log = LoggerFactory.getLogger(ActionDomain.class);

    private ArrayList<Sentence> baseSentences = new ArrayList<Sentence>();
    private HashMap<String, ArrayList<Sentence>> scenarios = new HashMap<String, ArrayList<Sentence>>();

    public ArrayList<String> actions = new ArrayList<String>(); //e.g., "(student,learn)"
    //public ArrayList<Actor> actors = new ArrayList<Actor>();
    //public ArrayList<Task> tasks = new ArrayList<Task>();
    public ArrayList<String> fluents = new ArrayList<String>(); //e.g., "night"

    public HashMap<String, ArrayList<Sentence>> fullScenarios = new HashMap<String, ArrayList<Sentence>>();

    private ArrayList<Query> queries = new ArrayList<Query>();
    public HashMap<String, ArrayList<Query>> mappedQueries = new HashMap<String, ArrayList<Query>>();
    public int minQueryID = Integer.MAX_VALUE;

    public ActionDomain() {
        ;//empty for now...
    }

    public void addQuery(Query query) {
        queries.add(query);
        if (query.id < minQueryID) {
            minQueryID = query.id;
        }
        log.debug("Added query: [" + query.toString() + "]");
    }

    public void addBaseSentence(Sentence sentence) {
        baseSentences.add(sentence);

        log.debug("Added sentence: [" + sentence.toString() + "]");
    }

    public void addScenarioSentence(String scenarioName, Sentence sentence) {
        if (scenarios.containsKey(scenarioName)) {
            scenarios.get(scenarioName).add(sentence);
        }
        else {
            scenarios.put(scenarioName, new ArrayList<Sentence>());
            scenarios.get(scenarioName).add(sentence);
        }

        log.debug("Added scenario: [" + scenarioName.toString() + "][" + sentence.toString() + "]");
    }

    public void addEmptyScenario(String scenarioName) {
        if (scenarios.containsKey(scenarioName) == false) {
            scenarios.put(scenarioName, new ArrayList<Sentence>());
        }
    }

    public void calculateFullScenarios() {
        for (String scenarioName : scenarios.keySet()) {
            ArrayList<Sentence> fullScenarioSentences = new ArrayList<Sentence>();
            for (Sentence baseSentence : baseSentences) {
                fullScenarioSentences.add(baseSentence);
            }
            for (Sentence scenarioSentence : scenarios.get(scenarioName)) {
                fullScenarioSentences.add(scenarioSentence);
            }
            fullScenarios.put(scenarioName, fullScenarioSentences);
        }
    }

    public void calculateMappedQueries() {
        for (Query oldQuery : queries) {
            String queryName = oldQuery.getScenarioName();

            if (mappedQueries.containsKey(queryName)) {
                ArrayList<Query> queriesInMap = mappedQueries.get(queryName);
                queriesInMap.add(oldQuery);
            }
            else {
                ArrayList<Query> queriesAL = new ArrayList<Query>();
                queriesAL.add(oldQuery);
                mappedQueries.put(queryName, queriesAL);
            }
        }
    }

    public void fillFluentsAndActionsIDs() {
        //baseSentences and scenarios
        for (Sentence sentence : baseSentences) {
            sentence.fillFluentAndActionIDs(fluents, actions);
        }
        for (String scenarioName : scenarios.keySet()) {
            for (Sentence sentence : scenarios.get(scenarioName)) {
                sentence.fillFluentAndActionIDs(fluents, actions);
            }
        }
        for (Query query : queries) {
            query.fillFluentAndActionIDs(fluents, actions);
        }
    }

    /*public void calculateElementToIntMappings() {
        ArrayList<Sentence> tempMergedSentences = new ArrayList<Sentence>();
        for (Sentence sentence : baseSentences) {
            tempMergedSentences.add(sentence);
        }
        for (String scenarioName : scenarios.keySet()) {
            for (Sentence sentence : scenarios.get(scenarioName)) {
                tempMergedSentences.add(sentence);
            }
        }

        for (Sentence sentence : tempMergedSentences) {

        }
    }*/

    //calculateIDsInSentences
    //buildFullScenarios
    //calculate HOENTs
    //respond to queries
}
