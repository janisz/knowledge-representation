initially (a && -b)
(Janek, A) occurs at 0
(Janek, A) releases a
(Janek, B) occurs at 1
(Janek, B) releases b if a
b triggers (Janek, C)

ScenarioOne {
  ACS = {
    ((Janek, D), 2)
    },
  OBS = {
  }
}

ever -a at 4 when ScenarioOne
ever (a && -b) at 4 when ScenarioOne
ever (-a || (a && -b)) at 4 when ScenarioOne
always (-a || (a && -b)) at 4 when ScenarioOne
