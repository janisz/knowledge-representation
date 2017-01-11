initially ((a && b) && c)
(Janek, A) releases a
(Janek, B) releases b if a

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

ever (a && b) at 9 when ScenarioOne
ever (a && -b) at 9 when ScenarioOne
ever (-a && b) at 9 when ScenarioOne
ever (-a && -b) at 9 when ScenarioOne
ever (a && b) at 9 when ScenarioTwo
ever (a && -b) at 9 when ScenarioTwo
ever (-a && b) at 9 when ScenarioTwo
ever (-a && -b) at 9 when ScenarioTwo
