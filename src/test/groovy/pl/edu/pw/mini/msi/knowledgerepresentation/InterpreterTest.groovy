package pl.edu.pw.mini.msi.knowledgerepresentation

import org.antlr.v4.runtime.misc.ParseCancellationException
import spock.lang.Specification
import spock.lang.Unroll

class InterpreterTest extends Specification {

    def interpreter = new Interpreter()

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

}
