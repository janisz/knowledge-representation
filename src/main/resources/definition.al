initially [-roomClosed, -hostelClosed, inHostel, -hasCard]
(Janek, closesDoor) causes [hostelClosed]
typically [-hasCard] triggers (Janek, takesCard)
typically (Janek, leaves) invokes (Janek, locksTheDoor)
(Janek, takesCard) causes [hasCard]
(Janek, leaves) causes [-inHostel]
(Janek, comeback) causes [inHostel] if [hasCard]
typically (DoorKeeper, lockTheDoor) occurs at 10


scenarioOne {
  ACS = {
      ((Janek, takesCard), 1),
      ((Janek, locksTheDoor), 3),
      ((Janek, comeback), 10)
    },
  OBS = {
      ([hasCard && inHostel], 4),
      ([-hasCard], 10)
  }
}


scenarioTwo {
  ACS = {
      ((Janek, takesCard), 3),
      ((Janek, locksTheDoor), 4),
      ((Janek, comeback), 10)
    },
  OBS = {
      ([-hasCard], 4),
      ([inHostel], 4),
      ([hasCard], 10)
  }
}

