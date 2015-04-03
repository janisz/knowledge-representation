package pl.edu.pw.mini.msi.knowledgerepresentation;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ConsoleErrorListener;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.pw.mini.msi.knowledgerepresentation.grammar.ActionLanguageLexer;
import pl.edu.pw.mini.msi.knowledgerepresentation.grammar.ActionLanguageParser;


public class Interpreter {

    private static final Logger log = LoggerFactory.getLogger(Interpreter.class);
    private final Context context;

    public Interpreter(Context context) {
        this.context = context;
    }

    public void eval(String input) {
        log.debug("Create a lexer and parser for input", input);
        ActionLanguageLexer lexer = new ActionLanguageLexer(new ANTLRInputStream(input));
        ActionLanguageParser parser = new ActionLanguageParser(new CommonTokenStream(lexer));
        parser.setErrorHandler(new BailErrorStrategy());
        ParseTreeWalker.DEFAULT.walk(new ActionLanguageListener(context), parser.programm());
    }

}
