typically (Janek, A) occurs at 0
typically (Janek, B) occurs at 1
(Janek, A) invokes (Janek, C)

scenarioEmpty {
  ACS = {
    },
  OBS = {
  }
}

ever performed (Janek, A) at 0 when scenarioEmpty
ever performed (Janek, B) at 1 when scenarioEmpty
ever performed (Janek, C) at 1 when scenarioEmpty
typically performed (Janek, A) at 0 when scenarioEmpty
typically performed (Janek, B) at 1 when scenarioEmpty
typically performed (Janek, C) at 1 when scenarioEmpty
