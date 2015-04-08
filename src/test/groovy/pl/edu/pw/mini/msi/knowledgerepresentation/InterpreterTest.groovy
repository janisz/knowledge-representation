package pl.edu.pw.mini.msi.knowledgerepresentation

import org.antlr.v4.runtime.misc.ParseCancellationException
import org.antlr.v4.runtime.tree.ParseTreeListener
import pl.edu.pw.mini.msi.knowledgerepresentation.data.Action
import pl.edu.pw.mini.msi.knowledgerepresentation.data.Actor
import pl.edu.pw.mini.msi.knowledgerepresentation.data.Fluent
import pl.edu.pw.mini.msi.knowledgerepresentation.data.Task
import pl.edu.pw.mini.msi.knowledgerepresentation.data.Time
import spock.lang.Specification
import spock.lang.Unroll

class InterpreterTest extends Specification {

    def errorListener = new ErrorListener()

    Context context
    Interpreter interpreter
    ActionLanguageListener parseTreeListener

    def setup() {
        context = new Context()
        parseTreeListener = new ActionLanguageListener(context)
        interpreter = new Interpreter(errorListener, parseTreeListener)
    }

    @Unroll
    def "should return OK when line ('#instruction') is valid instruction"() {
        expect:
        interpreter.eval(instruction)
        where:
        instruction << [
                'initially [hasBook]',
                'typically (DoorKeeper, lockTheDoor) occurs at 10'
        ]
    }

    @Unroll
    def "should throw when line ('#instruction') is NOT valid instruction"() {
        when:
        interpreter.eval(instruction)
        then:
        thrown ParseCancellationException
        where:
        instruction << [
                'initially hasBook',
                'initially [hasBook1]',
                'initially [hasBook] at',
                'typically (DoorKeeper, lockTheDoor) occurs 10'
        ]
    }

    @Unroll
    def "should throw when line ('#instruction') is NOT valid instruction but could be fixed by adding missing part"() {
        when:
        interpreter.eval(instruction)
        then:
        thrown ParseCancellationException
        where:
        instruction << [
                'initially [hasBook, ',
                'typically (DoorKeeper,',
                'typically (DoorKeeper, lockTheDoor) occurs'
        ]
    }

    @Unroll
    def "should count actors (#actors)"() {
        when:
        interpreter.eval(instruction)
        then:
        context.actors.toList() == actors
        where:
        instruction | actors
        'initially [hasBook]' | []
        'typically (DoorKeeper, lockTheDoor) occurs at 10' | [new Actor('DoorKeeper')]
        'typically (DoorKeeper, lockTheDoor) occurs at 10 (KeyMaker, makeKey) occurs at 11' | [new Actor('DoorKeeper'), new Actor('KeyMaker')]
        'always involved [DoorKeeper, KeyMaker] when scenarioOne' | [new Actor('DoorKeeper'), new Actor('KeyMaker')]
        'always involved [DoorKeeper, DoorKeeper] when scenarioOne' | [new Actor('DoorKeeper')]
    }

    def "should create empty scenario"() {
        when:
        interpreter.eval('scenario { ACS = {}, OBS = {} }')
        then:
        context.scenarios.size() == 1
    }

    def "should create scenario"() {
        given:
        def scenarionDefinition = '''scenario {
            ACS = {
                ((Janek, takesCard), 3),
                ((Janek, locksTheDoor), 4),
                ((Janek, comeback), 10)
            },
            OBS = {
                ([-hasCard, inHostel], 4),
                ([hasCard], 5),
                ([inHostel], 4)
            }
        }'''
        when:
        interpreter.eval(scenarionDefinition)
        then:
        context.actors.toList() == [new Actor('Janek')]
        context.scenarios.size() == 1
        context.scenarios['scenario'].actions.size() == 3
        context.scenarios['scenario'].actions[new Time(3)] == new Action(new Actor('Janek'), new Task('takesCard'))
        context.scenarios['scenario'].actions[new Time(4)] == new Action(new Actor('Janek'), new Task('locksTheDoor'))
        context.scenarios['scenario'].actions[new Time(10)] == new Action(new Actor('Janek'), new Task('comeback'))
        context.scenarios['scenario'].observations.size() == 3
        context.scenarios['scenario'].observations.get(new Time(5)).toList() == [new Fluent('hasCard', true)]
        context.scenarios['scenario'].observations.get(new Time(4)).toList() == [new Fluent('hasCard', false), new Fluent('inHostel', true)]
    }

    def "should create multiple scenarios"() {
        given:
        def scenarionDefinition = '''scenario {
            ACS = {
                ((Janek, takesCard), 3),
                ((Janek, locksTheDoor), 4),
                ((Janek, comeback), 10)
            },
            OBS = {
                ([-hasCard, inHostel], 4),
                ([hasCard], 5)
            }
        }
        scenarioTwo { ACS = {}, OBS = {} }
        scenarioThree {
            ACS = { ((DoorKeeper, locksTheDoor), 1) },
            OBS = { ([-doorAreLocked], 4), ([doorAreLocked], 2) }
        }'''
        when:
        interpreter.eval(scenarionDefinition)
        then:
        context.scenarios.size() == 3
        context.scenarios['scenario'].actions.size() == 3
        context.scenarios['scenario'].observations.size() == 3
        context.scenarios['scenarioTwo'].actions.size() == 0
        context.scenarios['scenarioTwo'].observations.size() == 0
        context.scenarios['scenarioThree'].actions.size() == 1
        context.scenarios['scenarioThree'].observations.size() == 2
    }

    @Unroll
    def "should set typically to #typically for #instruction"() {
        when:
        interpreter.eval(instruction)
        then:
        parseTreeListener.typically == typically
        where:
        instruction | typically
        'initially [hasBook]' | false
        '(DoorKeeper, lockTheDoor) occurs at 10' | false
        'typically (Janek, takesCard) causes [hasCard] after 11 if [-weekend]' | true
    }
}