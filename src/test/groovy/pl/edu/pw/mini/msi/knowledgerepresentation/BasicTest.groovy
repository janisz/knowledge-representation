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
    def "should find max time"() {
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
        '/definition_o_03a.al'    | 4     | 'true, null, true'
        '/definition_o_04.al'     | 4     | 'true, true'
        '/definition_o_05.al'     | 4     | 'true'
        '/definition_o_05a.al'    | 4     | 'true'
        '/definition_o_06.al'     | 4     | 'true, true, true, true'
        '/definition_o_07.al'     | 4     | 'true, true, true, false, false, false'
        '/definition_o_08.al'     | 4     | 'true'
        '/definition_o_09.al'     | 4     | 'null'
        '/definition_o_10.al'     | 9     | 'true, true, true, true'
        '/definition_o_11.al'     | 9     | 'true, true, true, true, true, true, true, true'
        '/definition_o_12.al'     | 16    | 'true, true, true, true, true, true, true, true'
        '/definition_o_13.al'     | 10    | 'true, true, true, true'
        '/definition_o_14.al'     | 10    | 'true, true, true, true'
        '/definition_o_15.al'     | 10    | 'true, true, true, true'
        '/definition_o_16.al'     | 8     | 'true, true, true, true, true, true'
        '/definition_o_17.al'     | 13    | 'true, true, true, true'
        '/definition_o_18.al'     | 13    | 'true, true, true, true, true, true'
        '/definition_o_19.al'     | 10    | 'true, true, true, false, true, true, true, false'
        '/definition_r_01.al'     | 6     | 'false'
        '/definition_r_02.al'     | 7     | 'true'
        '/definition_fapr96.al'   | 5     | 'true, true, true, true, true, true'
        '/definition_fapr96_02.al'| 5     | 'true, true, true, true, true, true, true'
	'/1-alternatywa'	  | 5     | 'true, true, true, true, true, false'
        '/2-koniunkcja'  	  | 5     | 'true, true, true, true, true'
        '/3-implikacja'  	  | 5     | 'true, true, true, true, true'
        '/4-prostyTrigger'  	  | 5     | 'true, true, true, false, true'
        '/5-releases'    	  | 5     | 'true, true, true, true, true, false'
        '/6-occurs'     	  | 5     | 'true, true, false, false'
        '/7-occursTypically'      | 5     | 'true, true, true, false, true'
        '/8-triggerTypically'     | 5     | 'true, true, true, true, false, false, true, false, false' //changed last result to false
        '/9-occursTypicallyQ'     | 5     | 'true, true, true, true, false, true, true, true, false, true'
        '/10-XOR_Typically'       | 5     | 'true, true, true, true, true, true, true, true, true, true'
        '/11-typicallyInChain'    | 5     | 'true, false, true, true, true, false, false, false, true, false, false' //changed two last to false, false
        '/12-typicallyInChain2'   | 5     | 'true, false, true, true, true, false, false, false, true, true, true'
        '/13-invokes'             | 5     | 'true, true, true, true'
        '/1-involved.al'          | 1     | 'false'
        '/2-involved.al'          | 3     | 'false, true, false, true, false, true, false, true'
    }
}
