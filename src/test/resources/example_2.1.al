initially (((-roomClosed && -hostelClosed) && inHostel) && -hasCard)
(Janek, closesDoor) causes hostelClosed
typically -hasCard triggers (Janek, takesCard)
typically (Janek, leaves) invokes (Janek, locksTheDoor)
(Janek, takesCard) causes hasCard
(Janek, leaves) causes -inHostel
(Janek, comeback) causes inHostel after 10 if hasCard
typically (DoorKeeper, lockTheDoor) occurs at 10


scenarioOne {
  ACS = {
      ((Janek, takesCard), 1),
      ((Janek, locksTheDoor), 3),
      ((Janek, comeback), 10)
    },
  OBS = {
      (hasCard, inHostel, 4),
      (-hasCard, 10)
  }
}


scenarioTwo {
  ACS = {
      ((Janek, takesCard), 3),
      ((Janek, locksTheDoor), 4),
      ((Janek, comeback), 10)
    },
  OBS = {
      (-hasCard, 4),
      (inHostel, 4),
      (hasCard, 10)
  }
}

always involved DoorKeeper when scenarioOne
typically (inHostel && -hasCard) at 11 when scenarioTwo
ever involved Janek, DoorKeeper when scenarioOne

