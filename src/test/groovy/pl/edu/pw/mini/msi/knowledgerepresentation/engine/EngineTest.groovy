package pl.edu.pw.mini.msi.knowledgerepresentation.engine

import pl.edu.pw.mini.msi.knowledgerepresentation.data.Action
import pl.edu.pw.mini.msi.knowledgerepresentation.data.Fluent
import spock.lang.Specification

class EngineTest extends Specification {

    def "should not throw exception if configuration is valid"() {
        given:
print """
initially  [dogHungry, -bowlFul]
[dogHungry, bowlFull] triggers (dog, eats)
(dog, eats) causes [-bowlFull] if [bowlFull, dogHungry]
[dogHungry, -bowlFull] triggers (dog, yelp)
(dog, yelp) invokes (owner, makeBowlFull) after 5 if [-bowlFull]
(owner, makeBowlFull) causes [bowlFull]
"""
        def knowledge = new Knowledge()
        knowledge._Initially.add(new Fluent("dogHungry", true))
        knowledge._Initially.add(new Fluent("bowlFull", false))
        Action dog_eats = new Action("dog", "eats")
        Action dog_yelp = new Action("dog", "yelp")
        Action owner_makeBowlFull = new Action("owner", "makeBowlFull")
        Action dog_commitSuicide = new Action("dog", "commitSuicide")
        EffectCauses ec_a = new EffectCauses(dog_eats, [new Fluent("bowlFull", false), new Fluent("dogHungry", false)], [new Fluent("bowlFull", true), new Fluent("dogHungry", true)])
        EffectCauses ec_b = new EffectCauses(owner_makeBowlFull, [new Fluent("bowlFull", true)], [])
        EffectInvokes ei_a = new EffectInvokes(dog_yelp, owner_makeBowlFull, 1, [new Fluent("bowlFull", false)])
        EffectTriggers et_a = new EffectTriggers([new Fluent("bowlFull", true), new Fluent("dogHungry", true)], dog_eats)
        EffectTriggers et_b = new EffectTriggers([new Fluent("bowlFull", false), new Fluent("dogHungry", true)], dog_yelp)

        EffectCauses tec_a = new EffectCauses(dog_yelp, [new Fluent("dogSad", true)], [])
        EffectCauses tec_b = new EffectCauses(dog_commitSuicide, [new Fluent("dogDead", true)], [])
        EffectTriggers tet_a = new EffectTriggers([new Fluent("dogSad", true)], dog_commitSuicide)


        knowledge._Couses.add(ec_a)
        knowledge._Couses.add(ec_b)
        knowledge._Invokes.add(ei_a)
        knowledge._Triggers.add(et_a)
        knowledge._Triggers.add(et_b)

        knowledge._TypicallyCouses.add(tec_a)
        knowledge._TypicallyCouses.add(tec_b)
        knowledge._TypicallyTriggers.add(tet_a)

        def em = new EngineManager(knowledge, new Scenario(), 8)
        when:
        em.Run()
        then:
        244 == em._Finished.size()

    }

    def "should not throw exception if configuration is valid for another configuration"() {
        given:
        def knowledge = new Knowledge()
        knowledge._Initially.add(new Fluent("roomClosed", false))
        knowledge._Initially.add(new Fluent("hostelClosed", false))
        knowledge._Initially.add(new Fluent("inHostel", true))
        knowledge._Initially.add(new Fluent("hasCard", false))

         Action Janek_closesDoor = new Action("Janek", "closesDoor")
         Action Janek_locksTheDoor = new Action("Janek", "locksTheDoor")
         Action Janek_takesCard = new Action("Janek", "takesCard")
         Action Janek_leaves = new Action("Janek", "leaves")
         Action Janek_comeBack = new Action("Janek", "comeBack")

         EffectCauses ec_a = new EffectCauses(Janek_locksTheDoor, [new Fluent("hostelClosed", true)], [])
         EffectCauses ec_b = new EffectCauses(Janek_takesCard, [new Fluent("hasCard", true)], [])
         EffectCauses ec_c = new EffectCauses(Janek_leaves, [new Fluent("inHostel", false)], [])
         EffectCauses ec_d = new EffectCauses(Janek_comeBack, [new Fluent("inHostel", true)], [new Fluent("hasCard", true)])

         EffectTriggers et_a = new EffectTriggers([new Fluent("hasCard", false)], Janek_takesCard)
         EffectTriggers et_b = new EffectTriggers([new Fluent("drunk", true)], Janek_leaves)

         EffectInvokes ei_a = new EffectInvokes(Janek_leaves, Janek_locksTheDoor, 1, [])

        knowledge._Couses.add(ec_a)
        knowledge._Couses.add(ec_b)
        knowledge._Couses.add(ec_c)
        knowledge._Couses.add(ec_d)

        knowledge._Invokes.add(ei_a)

        knowledge._Triggers.add(et_a)
        knowledge._Triggers.add(et_b)

        def always_a = [new Fluent("drunk", true)]
        knowledge._AlwaysList.add(always_a)


        knowledge._ListOfAllActions.add(Janek_closesDoor)
        knowledge._ListOfAllActions.add(Janek_locksTheDoor)
        knowledge._ListOfAllActions.add(Janek_takesCard)
        knowledge._ListOfAllActions.add(Janek_leaves)
        knowledge._ListOfAllActions.add(Janek_comeBack)

        def em = new EngineManager(knowledge, new Scenario(), 8)

        when:
        em.Run()

        then:
        1 == em._Finished.size()
    }

}
