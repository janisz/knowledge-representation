package pl.edu.pw.mini.msi.knowledgerepresentation

import alice.tuprolog.Prolog
import com.google.common.collect.HashMultimap
import com.google.common.collect.Maps
import org.antlr.v4.runtime.misc.ParseCancellationException
import pl.edu.pw.mini.msi.knowledgerepresentation.data.Action
import pl.edu.pw.mini.msi.knowledgerepresentation.data.Actor
import pl.edu.pw.mini.msi.knowledgerepresentation.data.Fluent
import pl.edu.pw.mini.msi.knowledgerepresentation.data.Scenario
import pl.edu.pw.mini.msi.knowledgerepresentation.data.Task
import pl.edu.pw.mini.msi.knowledgerepresentation.data.Time
import spock.lang.Specification
import spock.lang.Unroll

class InterpreterTest extends Specification {

    def errorListener = new ErrorListener()

    Context context
    Prolog engine
    Interpreter interpreter
    ActionLanguageListener parseTreeListener

    def setup() {
        context = new Context()
        engine = new Prolog()
        parseTreeListener = new ActionLanguageListener(context, engine)
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
        given:
        context.scenarios.put("scenarioOne", new Scenario("scenarioOne", HashMultimap.create(), Maps.newHashMap()))
        when:
        interpreter.eval(instruction)
        then:
        context.actors.toList() == actors
        where:
        instruction | actors
        'initially [hasBook]' | []
        'typically (DoorKeeper, lockTheDoor) occurs at 10' | [actor('DoorKeeper')]
        'typically (DoorKeeper, lockTheDoor) occurs at 10 (KeyMaker, makeKey) occurs at 11' | [actor('DoorKeeper'), actor('KeyMaker')]
        'always involved [DoorKeeper, KeyMaker] when scenarioOne' | [actor('DoorKeeper'), actor('KeyMaker')]
        'always involved [DoorKeeper, DoorKeeper] when scenarioOne' | [actor('DoorKeeper')]
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
        engine.solve("actor('Janek').").success
        !engine.solve("actor('DoorKeeper').").success
        "[scenario]" == engine.solve('findall(X, scenario(X), S).').getTerm('S').toString()
        "[hasCard,inHostel,hasCard,inHostel]" == engine.solve('findall(X, fluent(X), F).').getTerm('F').toString()
        context.actors.toList() == [actor('Janek')]
        context.scenarios.size() == 1
        context.scenarios['scenario'].actions.size() == 3
        context.scenarios['scenario'].actions[time(3)] == action('Janek', 'takesCard')
        context.scenarios['scenario'].actions[time(4)] == action('Janek', 'locksTheDoor')
        context.scenarios['scenario'].actions[time(10)] == action('Janek', 'comeback')
        context.scenarios['scenario'].observations.size() == 3
        context.scenarios['scenario'].observations.get(time(5)).toList() == [fluent('hasCard')]
        context.scenarios['scenario'].observations.get(time(4)).toList() == [fluent('hasCard').not(), fluent('inHostel')]
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
        engine.solve("actor('DoorKeeper').").success
        "[scenario,scenarioTwo,scenarioThree]" == engine.solve('findall(X, scenario(X), S).').getTerm('S').toString()
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

    Actor actor(String name) {
        return new Actor(name)
    }

    Task task(String name) {
        return new Task(name)
    }

    Time time(int time) {
        return new Time(time)
    }

    Fluent fluent(String name) {
        return new Fluent(name, true)
    }

    Action action(String actorName, String taskName) {
        return new Action(actor(actorName), task(taskName))
    }
}