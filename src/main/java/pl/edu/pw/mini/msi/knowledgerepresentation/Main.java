package pl.edu.pw.mini.msi.knowledgerepresentation;

import com.google.common.base.Joiner;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.pw.mini.msi.knowledgerepresentation.engine.Knowledge;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {

        Logger log = LoggerFactory.getLogger(Main.class);
        ANTLRErrorListener errorListener = new ErrorListener();
        Knowledge knowledge = new Knowledge();
        ParseTreeListener parseTreeListener = new ActionLanguageListener(knowledge);
        Interpreter interpreter = new Interpreter(errorListener, parseTreeListener);

        try {
            String code = IOUtils.toString(System.in);
            List<Interpreter.Return> returns = interpreter.eval(code);
            log.info(Joiner.on(", ").join(returns));
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e);
        }
    }
}