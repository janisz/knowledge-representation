initially [-roomClosed, -hostelClosed, inHostel, -hasCard]
(Janek, closesDoor) causes [hostelClosed]
typically [-hasCard] triggers (Janek, takesCard)
typically (Janek, leaves) invokes (Janek, locksTheDoor)
(Janek, takesCard) causes [hasCard]
(Janek, leaves) causes [-inHostel]
(Janek, comeback) causes [inHostel] after 10 if [hasCard]
typically (DoorKeeper, lockTheDoor) occurs at 10


scenarioOne {ACS = {((Janek, takesCard), 11),((Janek, locksTheDoor), 13),((Janek, comeback), 20)},OBS = {([hasCard, inHostel], 14),([-hasCard], 20)}}


scenarioTwo {ACS = {((Janek, takesCard), 13),((Janek, locksTheDoor), 14),((Janek, comeback), 20)},OBS = {([-hasCard], 14),([inHostel], 14),([hasCard], 20)}}

always involved [DoorKepper] when scenarioOne
typically [inHostel, -hasCard] at 11 when scenarioTwo
ever involved [Janek, DoorKepper] when scenarioOne

