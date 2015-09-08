scenarioEmpty {
  ACS = {
    },
  OBS = {
  }
}

scenarioI {
  ACS = {
        ((Janek, Shoot), 0),
        ((Jim, Die), 2)
    },
  OBS = {
  }
}

always involved Janek when scenarioEmpty
always involved Janek when scenarioI
ever involved Janek when scenarioEmpty
ever involved Janek when scenarioI

always involved Janek, Jim when scenarioEmpty
always involved Janek, Jim when scenarioI
ever involved Janek, Jim when scenarioEmpty
ever involved Janek, Jim when scenarioI