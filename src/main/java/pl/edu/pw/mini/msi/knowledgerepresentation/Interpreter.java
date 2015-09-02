package pl.edu.pw.mini.msi.knowledgerepresentation;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


public class Interpreter {

    private static final Logger log = LoggerFactory.getLogger(Interpreter.class);
    private final ANTLRErrorListener errorListener;
    private final ActionLanguageListener parseTreeListener;

    public Interpreter() {
        this(new ErrorListener(), new ActionLanguageListener());
    }

    public Interpreter(ANTLRErrorListener errorListener, ActionLanguageListener parseTreeListener) {
        this.errorListener = errorListener;
        this.parseTreeListener = parseTreeListener;
    }

    public List<Boolean> eval(String input) {
        return new ArrayList<Boolean>();
    }
}
