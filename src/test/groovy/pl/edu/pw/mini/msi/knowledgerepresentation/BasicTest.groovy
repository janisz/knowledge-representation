package pl.edu.pw.mini.msi.knowledgerepresentation

import alice.tuprolog.Prolog
import alice.tuprolog.SolveInfo
import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTree
import pl.edu.pw.mini.msi.knowledgerepresentation.grammar.*
import spock.lang.Specification
import spock.lang.Unroll

class BasicTest extends Specification {

    ActionLanguageLexer lexer;
    CommonTokenStream tokenStream;
    ActionLanguageParser parser;
    ParseTree tree;

    def "should check if lexer is working"() {
        given:
        lexer = new ActionLanguageLexer(new ANTLRInputStream('initially [-roomClosed, -hostelClosed, inHostel, -hasCard]'));
        tokenStream = new CommonTokenStream(lexer);
        expect:
        tokenStream.numberOfOnChannelTokens == 14
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

    def "should test if prolog engine is working"() {
        given:
        Prolog engine = new Prolog()

        when:
        SolveInfo info = engine.solve("append([1],[2,3],X).")

        then:
        '[1,2,3]' == info.getTerm('X').toString()
    }
}
