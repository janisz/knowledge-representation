initially ((a && b) && c)
(Janek, A) releases a
typically (Janek, B) invokes (Janek, C) after 3 if a
(Janek, C) causes -b

ScenarioOne {
  ACS = {
    ((Janek, A), 0),
    ((Janek, B), 3)
    },
  OBS = {
  }
}

ScenarioTwo {
  ACS = {
    ((Janek, A), 3),
    ((Janek, B), 6)
    },
  OBS = {
  }
}

ever b at 12 when ScenarioOne
ever -b at 12 when ScenarioOne
typically -b at 12 when ScenarioOne
ever b at 12 when ScenarioTwo
ever -b at 12 when ScenarioTwo
typically -b at 12 when ScenarioTwo
