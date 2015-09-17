initially ((a && b) && c)
typically a triggers (Janek, A)
(Janek, A) causes -a
(Janek, B) causes -c if a

ScenarioOne {
  ACS = {
    ((Janek, B), 3)
    },
  OBS = {
  }
}

ScenarioTwo {
  ACS = {
    ((Janek, B), 5)
    },
  OBS = {
  }
}

ever c at 8 when ScenarioOne
ever -c at 8 when ScenarioOne
ever c at 8 when ScenarioTwo
ever -c at 8 when ScenarioTwo