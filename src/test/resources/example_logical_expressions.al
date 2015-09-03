initially ((night || day) => sleep)
typically (Janek, GoToBed) causes sleep if night
typically (Janek, GoToBed) causes nap if ((sleep => night) && (night || day))
(Janek, GoToBed) invokes (Janek, WakeUp) after 8 if night
(Janek, WakeUp) releases tired after 1 if (-hangover && -headache)

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