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

always involved Jim when scenarioEmpty
always involved Jim when scenarioI
ever involved Jim when scenarioEmpty
ever involved Jim when scenarioI