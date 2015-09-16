package pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentences.Query;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentences.Sentence;
import pl.edu.pw.mini.msi.knowledgerepresentation.utils.ArrayListOfStringUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Usage:
 * 1. create
 * 2. add baseSentences, scenarios, queries
 * 3. calculateFullScenarios()
 * 4. calculateMappedQueries()
 * 5. fillFluentsAndActionsIDs()
 * <p/>
 * Created by Tomek on 2015-08-29.
 */
public class ActionDomain {

    private static final Logger log = LoggerFactory.getLogger(ActionDomain.class);

    private final ArrayList<Sentence> baseSentences = new ArrayList<Sentence>();
    private final HashMap<String, ArrayList<Sentence>> scenarios = new HashMap<String, ArrayList<Sentence>>();

    public final ArrayList<String> actions = new ArrayList<>(); //e.g., "(student,learn)"
    public final ArrayList<String> fluents = new ArrayList<String>(); //e.g., "night"

    public final HashMap<String, ArrayList<Sentence>> fullScenarios = new HashMap<String, ArrayList<Sentence>>();

    private final ArrayList<Query> queries = new ArrayList<Query>();
    public final HashMap<String, ArrayList<Query>> mappedQueries = new HashMap<String, ArrayList<Query>>();
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
        } else {
            scenarios.put(scenarioName, new ArrayList<>());
            scenarios.get(scenarioName).add(sentence);
        }

        log.debug("Added scenario: [" + scenarioName + "][" + sentence.toString() + "]");
    }

    public void addEmptyScenario(String scenarioName) {
        if (!scenarios.containsKey(scenarioName)) {
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
            } else {
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

        log.debug("Added [" + fluents.size() + "] fluents: [" + ArrayListOfStringUtils.myToString(fluents) + "]");
        log.debug("Added [" + actions.size() + "] actions: [" + ArrayListOfStringUtils.myToString(actions) + "]");
    }
}
