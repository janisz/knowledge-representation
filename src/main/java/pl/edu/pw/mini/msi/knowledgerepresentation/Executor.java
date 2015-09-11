package pl.edu.pw.mini.msi.knowledgerepresentation;

import org.antlr.runtime.tree.CommonTree;
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
import pl.edu.pw.mini.msi.knowledgerepresentation.hoents.HoentsSettings;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Tomek on 2015-09-03.
 */
public class Executor {
    private static final Logger log = LoggerFactory.getLogger(Executor.class);
    private final ANTLRErrorListener errorListener;
    private final ActionLanguageListener parseTreeListener;

    private static int counter = 0;

    private String errorInformation = "";

    private Executor(ANTLRErrorListener errorListener, ActionLanguageListener parseTreeListener) {
        this.errorListener = errorListener;
        this.parseTreeListener = parseTreeListener;
    }

    public Executor() {
        this(new ErrorListener(), new ActionLanguageListener(new ActionDomain()));
    }

    public String getErrorInformation() {
        return errorInformation;
    }

    public List<Boolean> getResults(String input, InputStream inputStream, int tMaxArg, HoentsSettings hoentsSettings) {
        //if (inputStream != null && inputStream instanceof BufferedInputStream) {
        //    String s = ((BufferedInputStream)inputStream).toString();
            counter++;
            log.debug("counter" + counter);
        //}

        byte tMax = (byte)tMaxArg;
        //log.debug("Create a lexer and parser for input", input);
        ActionLanguageLexer lexer = null;
        if (input != null) {
            lexer = new ActionLanguageLexer(new ANTLRInputStream(input));
        }
        else if (inputStream != null) {
            try {
                lexer = new ActionLanguageLexer(new ANTLRInputStream(inputStream));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ActionLanguageParser parser = new ActionLanguageParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.addErrorListener(errorListener);
        ParseTreeWalker.DEFAULT.walk(parseTreeListener, parser.programm());
        ActionDomain actionDomain = parseTreeListener.getActionDomain();

        //CommonTree tree = (CommonTree)parser.parse().getTree();
        //parser.programm().instruction(1)

        //byte tMax = 4; //important
        HashMap<Integer, Boolean> resultsHM = new HashMap<Integer,Boolean>();
        for (String scenarioName : actionDomain.mappedQueries.keySet()) {
            ArrayList<Query> queriesForScenario = actionDomain.mappedQueries.get(scenarioName);
            ArrayList<Sentence> fullScenario = actionDomain.fullScenarios.get(scenarioName);

            Hoents hoents = new Hoents(fullScenario, queriesForScenario, tMax, (byte)actionDomain.fluents.size(),
                    actionDomain.actions, hoentsSettings);
            ArrayList<Boolean> resultsForScenario = new ArrayList<Boolean>();
            try {
                resultsForScenario = hoents.getQueriesAnswers();
            }
            catch (Exception exc) {
                String error = "While processing scenario [" + scenarioName + "] occurred exception [" + exc.getMessage() +
                        "][" + exc.toString() + "]";
                log.debug(error);
                errorInformation += error + "\r\n";
                exc.printStackTrace();
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
    }
}
