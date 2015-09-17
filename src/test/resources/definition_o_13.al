initially ((a && b) && (c && d))
(Janek, A) releases a

(Janek, B) causes -c if a

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

ever c at 8 when ScenarioOne
ever -c at 8 when ScenarioOne
ever c at 8 when ScenarioTwo
ever -c at 8 when ScenarioTwo