package pl.edu.pw.mini.msi.knowledgerepresentation

import org.antlr.v4.runtime.misc.ParseCancellationException
import pl.edu.pw.mini.msi.knowledgerepresentation.data.*
import pl.edu.pw.mini.msi.knowledgerepresentation.engine.Knowledge
import spock.lang.Specification
import spock.lang.Unroll

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
        String code = getClass().getResource('/example_2.1.al').text
        expect:
        interpreter.eval(code)
    }

    def "Wykasz's test"() {
        given:
        def program = '''
initially [dogHungry, -dogDead]

[dogHungry, -dogDead] triggers (dog, Sad)
(dog, Sad) invokes (dog, CommitSuicide) after 5
(dog, CommitSuicide) causes [dogDead] if [dogHungry, -dogDead]

(dog, eats) causes [-dogHungry]

scenarioOne {
    ACS = {
    },
    OBS = {
    }
}

ever [dogHungry] at 0 when scenarioOne
ever [-dogHungry] at 1 when scenarioOne
always involved [dog] when scenarioOne
always involved [cat] when scenarioOne
ever performed (dog, CommitSuicide) at 4 when scenarioOne
ever performed (dog, CommitSuicide) at 5 when scenarioOne
'''
        expect:
        interpreter.eval(program)
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
        instruction                  | fluents
        'initially [hasBook]'        | [fluent('hasBook')]
        'initially [hasBook,-empty]' | [fluent('hasBook'), fluent('empty').not()]
    }

    @Unroll
    def "should populate always (#fluents)"() {
        when:
        interpreter.eval(instruction)
        then:
        fluents == knowledge._AlwaysList
        where:
        instruction               | fluents
        'always [hasBook]'        | [[fluent('hasBook')]]
        'always [hasBook,-empty]' | [[fluent('empty').not(),fluent('hasBook')]]
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
        interpreter.eval(scenarioDefinition)
        then:
        parseTreeListener.scenarios.size() == 1
        parseTreeListener.scenarios['scenario'].ACS.size() == 3
        parseTreeListener.scenarios['scenario'].OBS == [
                new ScenarioOBSPart([fluent('inHostel'), fluent('hasCard').not()], 4),
                new ScenarioOBSPart([fluent('hasCard')], 5),
        ]
        parseTreeListener.scenarios['scenario'].ACS == [
                new ScenarioACSPart(action('Janek', 'takesCard'), 3),
                new ScenarioACSPart(action('Janek', 'locksTheDoor'), 4),
                new ScenarioACSPart(action('Janek', 'comeback'), 10),
        ] as List
    }

    def "should create multiple scenarios"() {
        given:
        def scenarioDefinition = '''scenario {
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
        interpreter.eval(scenarioDefinition)
        then:
        parseTreeListener.scenarios.size() == 3
        parseTreeListener.scenarios['scenario'].ACS.size() == 3
        parseTreeListener.scenarios['scenario'].OBS.size() == 2
        parseTreeListener.scenarios['scenarioTwo'].ACS.size() == 0
        parseTreeListener.scenarios['scenarioTwo'].OBS.size() == 0
        parseTreeListener.scenarios['scenarioThree'].ACS.size() == 1
        parseTreeListener.scenarios['scenarioThree'].OBS.size() == 2
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
        '(dog, CommitSuicide) causes [dogDead] if [dogHungry, -dogDead]'       | false
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