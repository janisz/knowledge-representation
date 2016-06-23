initially (a && -b)
(Janek, A) occurs at 3
(Janek, A) releases a
(Janek, B) occurs at 6
(Janek, B) releases b if a
b triggers (Janek, C)

ScenarioOne {
  ACS = {
    ((Janek, D), 7)
    },
  OBS = {
  }
}

ever -a at 9 when ScenarioOne
ever (a && -b) at 9 when ScenarioOne
ever (-a || (a && -b)) at 9 when ScenarioOne
always (-a || (a && -b)) at 9 when ScenarioOne
