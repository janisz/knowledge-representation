(Janek, -A) occurs at 3


ScenarioOne {
  ACS = {
    ((Janek, A), 3)
    },
  OBS = {
  }
}

ScenarioTwo {
  ACS = {
    ((Janek, B), 3)
    },
  OBS = {
  }
}

always performed (Janek, A) at 3 when ScenarioOne
always performed (Janek, B) at 3 when ScenarioTwo