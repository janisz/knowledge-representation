(Janek, A) occurs at 0
typically (Janek, A) invokes (Janek, B)

ScenarioOne {
  ACS = {
    },
  OBS = {
  }
}

ScenarioTwo {
  ACS = {
    ((Janek, C), 1)
    },
  OBS = {
  }
}

typically performed (Janek, B) at 1 when ScenarioOne
typically performed (Janek, C) at 1 when ScenarioTwo