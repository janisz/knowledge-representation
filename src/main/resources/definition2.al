initially ((night || day) => sleep)
(Janek, -GoToBed) causes sleep if night
(Janek, GoToBed) causes nap if ((sleep => night) && (night || day))
(Janek, GoToBed) invokes (Janek, WakeUp) after 8 if night
(Janek, WakeUp) releases tired if (-hangover && -headache)
(Janek, DoSomething) occurs at 11

scenarioOne {
  ACS = {
      ((Janek, takesCard), 1),
      ((Janek, locksTheDoor), 3),
      ((Janek, comeback), 10)
    },
  OBS = {
      ((hasCard && inHostel), 4),
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

