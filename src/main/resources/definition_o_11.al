initially (a && b)
(Janek, A) releases a
(Janek, B) releases b

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
    ((Janek, A), 2),
    ((Janek, B), 5)
    },
  OBS = {
  }
}

ever (a && -b) at 8 when ScenarioOne
ever (-a && b) at 8 when ScenarioOne
ever (a && b) at 8 when ScenarioOne
ever (-a && -b) at 8 when ScenarioOne
ever (a && -b) at 8 when ScenarioTwo
ever (-a && b) at 8 when ScenarioTwo
ever (a && b) at 8 when ScenarioTwo
ever (-a && -b) at 8 when ScenarioTwo
