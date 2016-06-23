initially ((a && b) && c)
typically (Janek, A) invokes (Janek, B)
(Janek, B) causes -b if a

ScenarioOne {
  ACS = {
    ((Janek, A), 0)
    },
  OBS = {
  }
}

ScenarioTwo {
  ACS = {
    ((Janek, A), 3)
    },
  OBS = {
  }
}

ever b at 6 when ScenarioOne
ever -b at 6 when ScenarioOne
typically -b at 6 when ScenarioOne
ever b at 6 when ScenarioTwo
ever -b at 6 when ScenarioTwo
typically -b at 6 when ScenarioTwo
