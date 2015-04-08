package pl.edu.pw.mini.msi.knowledgerepresentation;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.pw.mini.msi.knowledgerepresentation.grammar.ActionLanguageLexer;
import pl.edu.pw.mini.msi.knowledgerepresentation.grammar.ActionLanguageParser;


public class Interpreter {

    private static final Logger log = LoggerFactory.getLogger(Interpreter.class);
    private final ANTLRErrorListener errorListener;
    private final ParseTreeListener parseTreeListener;

    public Interpreter(ANTLRErrorListener errorListener, ParseTreeListener parseTreeListener) {
        this.errorListener = errorListener;
        this.parseTreeListener = parseTreeListener;
    }

    public void eval(String input) {
        log.debug("Create a lexer and parser for input", input);
        ActionLanguageLexer lexer = new ActionLanguageLexer(new ANTLRInputStream(input));
        ActionLanguageParser parser = new ActionLanguageParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.addErrorListener(errorListener);
        ParseTreeWalker.DEFAULT.walk(parseTreeListener, parser.programm());
    }
}
