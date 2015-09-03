package pl.edu.pw.mini.msi.knowledgerepresentation;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.ActionDomain;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentences.Query;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentences.Sentence;
import pl.edu.pw.mini.msi.knowledgerepresentation.grammar.ActionLanguageLexer;
import pl.edu.pw.mini.msi.knowledgerepresentation.grammar.ActionLanguageParser;
import pl.edu.pw.mini.msi.knowledgerepresentation.hoents.Hoents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Tomek on 2015-09-03.
 */
public class Executor {/*
    private static final Logger log = LoggerFactory.getLogger(Interpreter.class);
    private final ANTLRErrorListener errorListener;
    private final ActionLanguageListenerDeprecated parseTreeListener;

    private Executor(ANTLRErrorListener errorListener, ActionLanguageListenerDeprecated parseTreeListener) {
        this.errorListener = errorListener;
        this.parseTreeListener = parseTreeListener;
    }

    public Executor() {
        this(new ErrorListener(), new ActionLanguageListenerDeprecated(new ActionDomain()));
    }

    public List<Boolean> getResults(String input) {
        //log.debug("Create a lexer and parser for input", input);
        ActionLanguageLexer lexer = new ActionLanguageLexer(new ANTLRInputStream(input));
        ActionLanguageParser parser = new ActionLanguageParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.addErrorListener(errorListener);
        ParseTreeWalker.DEFAULT.walk(parseTreeListener, parser.programm());
        ActionDomain actionDomain = parseTreeListener.getActionDomain();

        byte tMax = 5; //important
        HashMap<Integer, Boolean> resultsHM = new HashMap<Integer,Boolean>();
        for (String scenarioName : actionDomain.mappedQueries.keySet()) {
            ArrayList<Query> queriesForScenario = actionDomain.mappedQueries.get(scenarioName);
            ArrayList<Sentence> fullScenario = actionDomain.fullScenarios.get(scenarioName);

            Hoents hoents = new Hoents(fullScenario, queriesForScenario, tMax, (byte)actionDomain.fluents.size(),
                    actionDomain.actions);
            ArrayList<Boolean> resultsForScenario = new ArrayList<Boolean>();
            try {
                resultsForScenario = hoents.getQueriesAnswers();
            }
            catch (Exception exc) {
                log.debug("While processing scenario [%s] occured exception [%s].", scenarioName, exc.getMessage());
                for (int index = 0; index < queriesForScenario.size(); index++) {
                    resultsForScenario.add(null);
                }
            }
            for (byte queryIDIndex = 0; queryIDIndex < queriesForScenario.size(); queryIDIndex++) {
                resultsHM.put(queriesForScenario.get(queryIDIndex).id, resultsForScenario.get(queryIDIndex));
            }
        }
        ArrayList<Boolean> results = new ArrayList<Boolean>();
        for (int queryIDindex = actionDomain.minQueryID; queryIDindex < actionDomain.minQueryID + resultsHM.keySet().size();
                queryIDindex++) {
            results.add(resultsHM.get(queryIDindex));
        }
        return results;
    }*/
}
