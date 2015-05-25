package pl.edu.pw.mini.msi.knowledgerepresentation

import org.antlr.v4.runtime.misc.ParseCancellationException
import pl.edu.pw.mini.msi.knowledgerepresentation.data.*
import pl.edu.pw.mini.msi.knowledgerepresentation.util.FileLoader
import pl.edu.pw.mini.msi.knowledgerepresentation.engine.Knowledge
import spock.lang.Specification
import spock.lang.Unroll
import java.net.URL
import java.lang.ClassLoader

class InterpreterTest extends Specification {

    def errorListener = new ErrorListener()

    Knowledge knowledge
    Interpreter interpreter
    ActionLanguageListener parseTreeListener

    def setup() {
        knowledge = new Knowledge()
        parseTreeListener = new ActionLanguageListener(knowledge)
        interpreter = new Interpreter(errorListener, parseTreeListener)
    }
	
    def "should work for example code"() {
        given:
        	String file = getClass().getClassLoader().getResource("example_2.1.al").getFile()
			String document = FileLoader.parseTestFileAsString(file)
        expect:
        	interpreter.eval(document)
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
    def "should populate fluents (#fluents)"() {
        when:
        interpreter.eval(instruction)
        then:
        knowledge._Initially.containsAll(fluents)
        where:
        instruction           | fluents
        'initially [hasBook]' | [fluent('hasBook')]
        'initially [hasBook,-empty]' | [fluent('hasBook'), fluent('empty').not()]
    }

    @Unroll
    def "should populate always (#fluents)"() {
        when:
        interpreter.eval(instruction)
        then:
        fluents == knowledge._AlwaysList
        where:
        instruction           | fluents
        'always [hasBook]' | [[fluent('hasBook')]]
        'always [hasBook,-empty]' | [[fluent('hasBook'), fluent('empty').not()]]
    }

    def "should create empty scenario"() {
        when:
        interpreter.eval('scenario { ACS = {}, OBS = {} }')
        then:
        parseTreeListener.scenarios.size() == 1
    }

    def "should create scenario"() {
        given:
        def scenarioDefinition = '''scenario {
            ACS = {
                ((Janek, takesCard), 13),
                ((Janek, locksTheDoor), 14),
                ((Janek, comeback), 20)
            },
            OBS = {
                ([-hasCard, inHostel], 14),
                ([hasCard], 15),
                ([inHostel], 14)
            }
        }'''
        when:
        interpreter.eval(scenarioDefinition)
        then:
        parseTreeListener.scenarios.size() == 1
        parseTreeListener.scenarios['scenario'].actions.size() == 3
        parseTreeListener.scenarios['scenario'].actions[time(13)] == action('Janek', 'takesCard')
        parseTreeListener.scenarios['scenario'].actions[time(14)] == action('Janek', 'locksTheDoor')
        parseTreeListener.scenarios['scenario'].actions[time(20)] == action('Janek', 'comeback')
        parseTreeListener.scenarios['scenario'].observations.size() == 3
        parseTreeListener.scenarios['scenario'].observations.get(time(15)).toList() == [fluent('hasCard')]
        parseTreeListener.scenarios['scenario'].observations.get(time(14)).toList() == [fluent('hasCard').not(), fluent('inHostel')]
    }
	
    def "should create multiple scenarios"() {
        given:
        def scenarioDefinition = '''scenario {
            ACS = {
                ((Janek, takesCard), 13),
                ((Janek, locksTheDoor), 14),
                ((Janek, comeback), 20)
            },
            OBS = {
                ([-hasCard, inHostel], 14),
                ([hasCard], 15)
            }
        }
        scenarioTwo { ACS = {}, OBS = {} }
        scenarioThree {
            ACS = { ((DoorKeeper, locksTheDoor), 11) },
            OBS = { ([-doorAreLocked], 14), ([doorAreLocked], 12) }
        }'''
        when:
        interpreter.eval(scenarioDefinition)
        then:
        parseTreeListener.scenarios.size() == 3
        parseTreeListener.scenarios['scenario'].actions.size() == 3
        parseTreeListener.scenarios['scenario'].observations.size() == 3
        parseTreeListener.scenarios['scenarioTwo'].actions.size() == 0
        parseTreeListener.scenarios['scenarioTwo'].observations.size() == 0
        parseTreeListener.scenarios['scenarioThree'].actions.size() == 1
        parseTreeListener.scenarios['scenarioThree'].observations.size() == 2
    }

    @Unroll
    def "should set typically to #typically for #instruction"() {
        when:
        interpreter.eval(instruction)
        then:
        parseTreeListener.typically == typically
        where:
        instruction                                                            | typically
        'initially [hasBook]'                                                  | false
        '(DoorKeeper, lockTheDoor) occurs at 10'                               | false
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