package pl.edu.pw.mini.msi.knowledgerepresentation

import com.google.common.base.Joiner
import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTree
import pl.edu.pw.mini.msi.knowledgerepresentation.grammar.ActionLanguageLexer
import pl.edu.pw.mini.msi.knowledgerepresentation.grammar.ActionLanguageParser
import pl.edu.pw.mini.msi.knowledgerepresentation.hoents.HoentsSettings
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
    def "proceeding file '#filename' with params (#doThrow,#doThrIfTm) returned #expectedResults"() {
        given:
        def code = getClass().getResourceAsStream(filename).text;

        List<Boolean> actualResults = new Executor().getResults(code, tMax,
                new HoentsSettings(doThrow, doThrIfTm));
        //List<Boolean> expectedResults = new ArrayList<Boolean>();

        expect:
        actualResults == expectedResults

        where:
        filename                  |tMax   | doThrow | doThrIfTm | expectedResults
        '/definition_w_01.al'     | 15    | true    | false     | [true, true, true, true, true, true]
        '/definition_w_02.al'     | 15    | true    | false     | [true, true, false, true, true, true, true, true]
        '/definition_w_06.al'     | 15    | true    | false     | [true, true, true, true, false, false, false]
        '/definition_w_12.al'     | 15    | true    | false     | [true, true]
        '/definition_w_14.al'     | 15    | true    | false     | [true, true, false, true, true, true, true, true]
        '/definition_o_01.al'     | 4     | true    | false     | [true]
        '/definition_o_02.al'     | 4     | true    | false     | [true, true]
        '/definition_o_03.al'     | 4     | true    | false     | [true, true]
        '/definition_o_03a.al'    | 4     | true    | false     | [true, null, true]
        '/definition_o_04.al'     | 4     | true    | false     | [true, true]
        '/definition_o_05.al'     | 4     | true    | false     | [true]
        '/definition_o_05a.al'    | 4     | true    | false     | [true]
        '/definition_o_06.al'     | 4     | true    | false     | [true, true, true, true]
        '/definition_o_07.al'     | 4     | true    | false     | [true, true, true, false, false, false]
        '/definition_o_08.al'     | 4     | true    | false     | [true]
        '/definition_o_09.al'     | 4     | true    | false     | [null] //contradictory at sentences
        '/definition_o_10.al'     | 9     | true    | false     | [true, true, true, true]
        '/definition_o_11.al'     | 9     | true    | false     | [true, true, true, true, true, true, true, true]
        '/definition_o_12.al'     | 16    | true    | false     | [true, true, true, true, true, true, true, true]
        '/definition_o_13.al'     | 10    | true    | false     | [true, true, true, true]
        '/definition_o_14.al'     | 10    | true    | false     | [true, true, true, true]
        '/definition_o_15.al'     | 10    | true    | false     | [true, true, true, true]
        '/definition_o_16.al'     | 8     | true    | false     | [true, true, true, true, true, true]
        '/definition_o_17.al'     | 13    | true    | false     | [true, true, true, true]
        '/definition_o_18.al'     | 13    | true    | false     | [true, true, true, true, true, true]
        '/definition_o_19.al'     | 10    | true    | false     | [true, true, true, false, true, true, true, false]
        '/definition_o_20.al'     | 11    | true    | false     | [false] //typically invokes "conflicting" with observation
        '/definition_o_21.al'     | 11    | true    | false     | [false] //typically occurs "conflicting" with observation
        '/definition_o_22.al'     | 5     | true    | false     | [null, true] //occurs -A
        '/definition_o_23.al'     | 5     | true    | false     | [null, null, true, true] //triggers -A
        '/definition_o_24.al'     | 10    | true    | false     | [null, null, true, true] //invokes -A
        '/definition_o_25.al'     | 10    | true    | false     | [null, null, true, true] //releases, invokes -A, occurs A
        '/definition_o_25a.al'    | 10    | true    | false     | [null, null, true, true] //releases, invokes -A, occurs A, time difference == 1

        '/definition_o_26.al'     | 2     | true    | true      | [null] //occurs, invokes, time NOT sufficient
        '/definition_o_26.al'     | 2     | true    | false     | [true] //occurs, invokes, time NOT sufficient

        '/definition_o_27.al'     | 8     | true    | false     | [null] //initially, occurs, causes, contradictory with "at" sentence observation
        '/definition_o_27a.al'    | 8     | true    | false     | [null] //initially, occurs, causes, contradictory with "at" sentence observation, time difference == 1
        '/definition_o_27b.al'    | 8     | true    | false     | [null] //initially, occurs, causes, contradictory with "at" sentence observation
        '/definition_o_27c.al'    | 8     | true    | false     | [null] //initially, occurs, causes, contradictory with "at" sentence observation, time difference == 1
        '/definition_o_28.al'     | 8     | true    | false     | [true, true] //initially, typically occurs, causes, only typical execution consistent with "at sentence" observation
        '/definition_o_29.al'     | 8     | true    | false     | [true, true] //initially, typically occurs, causes, only atypical execution consistent with "at sentence" observation
        '/definition_o_30.al'     | 10    | true    | false     | [true, false, null, null] //initially, releases, invokes if; conflicting actions

        '/definition_o_31.al'     | 10    | true    | false     | [null] //conflicting causes
        '/definition_o_31a.al'    | 10    | true    | false     | [null] //conflicting causes
        '/definition_o_32.al'     | 10    | true    | false     | [null] //conflicting invokes
        '/definition_o_32a.al'    | 10    | true    | false     | [null] //conflicting invokes
        '/definition_o_32b.al'    | 10    | true    | false     | [null] //conflicting invokes
        '/definition_o_32c.al'    | 10    | true    | false     | [null] //conflicting invokes
        '/definition_o_33.al'     | 10    | true    | false     | [null] //conflicting occurs at
        '/definition_o_33a.al'    | 10    | true    | false     | [null] //conflicting occurs at
        '/definition_o_33b.al'    | 10    | true    | false     | [null] //conflicting occurs at
        '/definition_o_34.al'     | 10    | true    | false     | [null] //conflicting triggers
        '/definition_o_34a.al'    | 10    | true    | false     | [null] //conflicting triggers
        '/definition_o_34b.al'    | 10    | true    | false     | [null] //conflicting triggers
        '/definition_o_34c.al'    | 10    | true    | false     | [null] //conflicting triggers

        //releases, one branch ok, second not ok, DOTHROW == true
        '/definition_o_35.al'     | 11    | true    | false     | [null, null, null, null] //releases, causes, one branch ok, second not ok
        '/definition_o_36.al'     | 10    | true    | false     | [true, false, null, null]  //releases, invokes, one branch ok, second not ok
        '/definition_o_36a.al'    | 10    | true    | false     | [true, false, null, null]  //releases, invokes, one branch ok, second not ok
        '/definition_o_37.al'     | 10    | true    | false     | [true, false] //releases, releases, one branch ok, second not ok
        '/definition_o_37a.al'    | 10    | true    | false     | [true, false] //releases, releases, one branch ok, second not ok
        '/definition_o_37b.al'    | 5     | true    | false     | [null, null, null, null] //releases, releases, one branch ok, second not ok
        '/definition_o_37c.al'    | 11    | true    | false     | [null, null, null, null] //releases, releases, one branch ok, second not ok
        '/definition_o_37d.al'    | 11    | true    | false     | [null, null, null, null, null] //releases, releases, one branch ok, second not ok
        '/definition_o_37e.al'    | 11    | true    | false     | [null, null, null, null, null] //releases, releases, one branch ok, second not ok
        '/definition_o_37f.al'    | 9     | true    | false     | [null, null, null, null, null] //releases, releases, one branch ok, second not ok
        '/definition_o_37g.al'    | 9     | true    | false     | [null, null, null, null, null] //releases, releases, one branch ok, second not ok
        '/definition_o_38.al'     | 10    | true    | false     | [true, false] //releases, triggers, one branch ok, second not ok
        '/definition_o_38a.al'    | 10    | true    | false     | [true, false] //releases, triggers, one branch ok, second not ok
        '/definition_o_39.al'     | 10    | true    | false     | [null, null, null, null] //releases, at, one branch ok, second not ok

        //releases, one branch ok, second not ok, DOTHROW == false
        '/definition_o_35.al'     | 11    | false   | false     | [true, true, true, true] //releases, causes, one branch ok, second not ok
        '/definition_o_36.al'     | 10    | false   | false     | [true, false, false, false]  //releases, invokes, one branch ok, second not ok
        '/definition_o_36a.al'    | 10    | false   | false     | [true, false, false, false]  //releases, invokes, one branch ok, second not ok
        '/definition_o_37.al'     | 10    | false   | false     | [true, false] //releases, releases, one branch ok, second not ok
        '/definition_o_37a.al'    | 10    | false   | false     | [true, false] //releases, releases, one branch ok, second not ok
        '/definition_o_37b.al'    | 5     | false   | false     | [true, true, true, true] //releases, releases, one branch ok, second not ok
        '/definition_o_37c.al'    | 11    | false   | false     | [true, true, true, true] //releases, releases, one branch ok, second not ok
        '/definition_o_37d.al'    | 11    | false   | false     | [true, true, true, true, true] //releases, releases, one branch ok, second not ok
        '/definition_o_37e.al'    | 11    | false   | false     | [true, true, true, true, true] //releases, releases, one branch ok, second not ok
        '/definition_o_37f.al'    | 9     | false   | false     | [true, true, true, true, true] //releases, releases, one branch ok, second not ok
        '/definition_o_37g.al'    | 9     | false   | false     | [true, true, true, true, true] //releases, releases, one branch ok, second not ok
        '/definition_o_38.al'     | 10    | false   | false     | [true, false] //releases, triggers, one branch ok, second not ok
        '/definition_o_38a.al'    | 10    | false   | false     | [true, false] //releases, triggers, one branch ok, second not ok
        '/definition_o_39.al'     | 10    | false   | false     | [true, true, true, true] //releases, at, one branch ok, second not ok

        '/definition_o_40.al'     | 8     | true    | false     | [true] //typically invokes, one branch ok, second not ok
        '/definition_o_40a.al'    | 11    | true    | false     | [true, true] //typically invokes, one branch ok, second not ok
        '/definition_o_41.al'     | 8     | true    | false     | [true, true, true, true] //typically occurs at, one branch ok, second not ok
        '/definition_o_41a.al'    | 8     | true    | false     | [true, false, true, false] //typically occurs at, one branch ok, second not ok
        '/definition_o_42.al'     | 4     | true    | false     | [true, true, true, true] //typically triggers, one branch ok, second not ok

        //two typicalities at the same time 'conflict'
        '/definition_o_43.al'     | 7     | true    | false     | [false, true, false, true] //invokes
        '/definition_o_43a.al'    | 5     | true    | false     | [false, true, false, true] //invokes
        '/definition_o_44.al'     | 2     | true    | false     | [false, true, false, true] //triggers
        '/definition_o_45.al'     | 2     | true    | false     | [false, true, false, true] //occurs at
        '/definition_o_45a.al'    | 4     | true    | false     | [false, true, false, true] //occurs at

        //complicated scenarios
        '/definition_o_46.al'     | 5     | true    | false     | [true] //
        '/definition_o_46a.al'    | 7     | true    | false     | [true] //
        '/definition_o_47.al'     | 5     | true    | false     | [true] //
        '/definition_o_47a.al'    | 8     | true    | false     | [true] //
        '/definition_o_48.al'     | 8     | true    | false     | [true] //
        '/definition_o_48a.al'    | 8     | true    | false     | [true] //
        '/definition_o_49.al'     | 8     | true    | false     | [true, true, true] //
        '/definition_o_49a.al'    | 9     | true    | false     | [true, true, true] //

        '/definition_r_01.al'     | 6     | true    | false     | [false]
        '/definition_r_02.al'     | 7     | true    | false     | [true]
        '/definition_fapr96.al'   | 5     | true    | false     | [true, true, true, true, true, true]
        '/definition_fapr96_02.al'| 5     | true    | false     | [true, true, true, true, true, true, true]
        '/definition_fapr96_03.al'| 5     | true    | false     | [true, true, true, true, true, true, true]

	    '/1-alternatywa'	      | 5     | true    | false     | [true, true, true, true, true, false]
        '/2-koniunkcja'  	      | 5     | true    | false     | [true, true, true, true, true]
        '/3-implikacja'  	      | 5     | true    | false     | [true, true, true, true, true]
        '/4-prostyTrigger'  	  | 5     | true    | false     | [true, true, true, false, true]
        '/5-releases'    	      | 5     | true    | false     | [true, true, true, true, true, false]
        '/6-occurs'     	      | 5     | true    | false     | [true, true, false, false]
        '/7-occursTypically'      | 5     | true    | false     | [true, true, true, false, true]
        '/8-triggerTypically'     | 5     | true    | false     | [true, true, true, true, false, false, true, false, true] //changed last result to false
        '/9-occursTypicallyQ'     | 5     | true    | false     | [true, true, true, true, false, true, true, true, false, true]
        '/10-XOR_Typically'       | 5     | true    | false     | [true, true, true, true, true, true, true, true, true, true]
        '/11-typicallyInChain'    | 4     | true    | false     | [false, false, true, true, true, false, false, false, true, true, true] //changed two last to false, false //changed first to false
        '/12-typicallyInChain2'   | 5     | true    | false     | [true, false, true, true, true, false, false, false, true, true, true]
        '/13-invokes'             | 5     | true    | false     | [true, true, true, true]
        '/1-involved.al'          | 1     | true    | false     | [false]
        '/2-involved.al'          | 3     | true    | false     | [false, true, false, true, false, true, false, true]
        '/14-actions-1'          | 3     | true    | false     | [true, false, false, true]
    }
}
