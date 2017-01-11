initially a
(Janek, A) releases a

ScenarioOne {
  ACS = {
    ((Janek, A), 0)
    },
  OBS = {
  }
}

ScenarioTwo {
  ACS = {
    ((Janek, A), 2)
    },
  OBS = {
  }
}

ever a at 4 when ScenarioOne
ever -a at 4 when ScenarioOne
ever a at 4 when ScenarioTwo
ever -a at 4 when ScenarioTwo
