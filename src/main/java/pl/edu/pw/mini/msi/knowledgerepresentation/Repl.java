package pl.edu.pw.mini.msi.knowledgerepresentation;

import com.google.common.collect.ImmutableSet;
import jline.console.ConsoleReader;
import jline.console.completer.StringsCompleter;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.pw.mini.msi.knowledgerepresentation.engine.Knowledge;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;

public class Repl {
    private final static Collection<String> KEYWORDS = ImmutableSet.of(
            "ACS", "after", "always", "at", "causes", "ever",
            "generally", "if", "imposible", "initially",
            "invokes", "involved", "-", "OBS", "perfomed",
            "releases", "triggers", "typically", "when");

    private final static Logger log = LoggerFactory.getLogger(Repl.class);
    private final static ANTLRErrorListener errorListener = new ErrorListener();
    private final static Knowledge knowledge = new Knowledge();
    private final static ParseTreeListener parseTreeListener = new ActionLanguageListener(knowledge);
    private final static Interpreter interpreter = new Interpreter(errorListener, parseTreeListener);

    public static void main(String[] args) throws IOException {

        ConsoleReader reader = new ConsoleReader();
        PrintWriter out = new PrintWriter(reader.getOutput());

        reader.setPrompt("\u001B[1m~\u001B[32m>\u001B[0m ");
        reader.addCompleter(new StringsCompleter(KEYWORDS));

        String line;
        while ((line = reader.readLine()) != null) {
            out.println("\u001B[33m======>\u001B[0m\"" + line + "\"");
            out.flush();

            try {
                List<Interpreter.Return> returns = interpreter.eval(line);
                out.println(String.format("%s", returns));
            } catch (RuntimeException e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}