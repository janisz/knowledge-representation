package pl.edu.pw.mini.msi.knowledgerepresentation;

import com.google.common.base.Joiner;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {

        Logger log = LoggerFactory.getLogger(Main.class);
        ANTLRErrorListener errorListener = new ErrorListener();
        ActionLanguageListener parseTreeListener = new ActionLanguageListener();
        Interpreter interpreter = new Interpreter(errorListener, parseTreeListener);

        try {
            String code = IOUtils.toString(System.in);
            List<Boolean> returns = interpreter.eval(code);
            log.info(Joiner.on(", ").useForNull("null").join(returns));
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e);
        }
    }
}