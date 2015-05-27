package pl.edu.pw.mini.msi.knowledgerepresentation;

import org.antlr.v4.runtime.ConsoleErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorListener extends ConsoleErrorListener {

    private static final Logger log = LoggerFactory.getLogger(ErrorListener.class);

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        log.error(msg, e);
        throw new ParseCancellationException(msg, e);
    }
}
