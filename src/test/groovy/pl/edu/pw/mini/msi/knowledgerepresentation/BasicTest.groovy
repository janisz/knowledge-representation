package pl.edu.pw.mini.msi.knowledgerepresentation

import com.google.common.base.Joiner
import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTree
import pl.edu.pw.mini.msi.knowledgerepresentation.grammar.ActionLanguageLexer
import pl.edu.pw.mini.msi.knowledgerepresentation.grammar.ActionLanguageParser
import spock.lang.Specification
import spock.lang.Unroll

class BasicTest extends Specification {

    ActionLanguageLexer lexer;
    CommonTokenStream tokenStream;
    ActionLanguageParser parser;
    ParseTree tree;

    def "should check if lexer is working"() {
        given:
        lexer = new ActionLanguageLexer(new ANTLRInputStream('initially (((-roomClosed && -hostelClosed) && inHostel) && -hasCard)'));
        tokenStream = new CommonTokenStream(lexer);
        expect:
        tokenStream.numberOfOnChannelTokens == 18
    }

    def
    "should parse al with logical expressions"() {
        given:
        InputStream resourceAsStream = getClass().getResourceAsStream("/example_logical_expressions.al");
        ANTLRInputStream antlrInputStream = new ANTLRInputStream(resourceAsStream);
        lexer = new ActionLanguageLexer(antlrInputStream);
        tokenStream = new CommonTokenStream(lexer);
        parser = new ActionLanguageParser(tokenStream)
        tree = parser.programm()
        expect:
        tree.toStringTree(parser).count('entry ') == 4
    }

    @Unroll
    def "should find node '#node' #count times in parse tree "() {
        given:
        InputStream resourceAsStream = getClass().getResourceAsStream("/example_2.1.al");
        ANTLRInputStream antlrInputStream = new ANTLRInputStream(resourceAsStream);
        lexer = new ActionLanguageLexer(antlrInputStream);
        tokenStream = new CommonTokenStream(lexer);
        parser = new ActionLanguageParser(tokenStream)
        tree = parser.programm()
        expect:
        tree.toStringTree(parser).count(node) == count

        where:
        node        | count
        'entry '    | 7
        'scenario ' | 2
        'query '    | 3
    }

    @Unroll
    def "proceeding file '#filename' returned #expectedResults"() {
        given:
        InputStream resourceAsStream = getClass().getResourceAsStream(filename);

        List<Boolean> actualResults = new Executor().getResults(null, resourceAsStream, tMax);
        //List<Boolean> expectedResults = new ArrayList<Boolean>();

        expect:
        Joiner.on(", ").useForNull("null").join(actualResults).equals( expectedResults )

        where:
        filename                  |tMax   | expectedResults
        '/definition_w_01.al'     | 15    | 'true, true, true, true, true, true'
        '/definition_w_02.al'     | 15    | 'true, true, false, true, true, true, true, true'
        '/definition_w_06.al'     | 15    | 'true, true, true, true, false, false, false'
        '/definition_w_12.al'     | 15    | 'true, true'
        '/definition_w_14.al'     | 15    | 'true, true, false, true, true, true, true, true'
        '/definition_o_01.al'     | 4     | 'true'
        '/definition_o_02.al'     | 4     | 'true, true'
        '/definition_o_03.al'     | 4     | 'true, true'
        //'/definition_o_03a.al'     | 4     | 'true, true'
        '/definition_o_04.al'     | 4     | 'true, true'
        '/definition_o_05.al'     | 4     | 'true'
        '/definition_o_05a.al'    | 4     | 'true'
        '/definition_fapr96.al'   | 5     | 'true, true, true, true, true, true'
        '/definition_fapr96_02.al'| 5     | 'true, true, true, true, true, true, true'
    }
}
