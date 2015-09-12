initially (a && b)
(Janek, A) occurs at 0
(Janek, A) releases a
(Janek, B) occurs at 1
(Janek, B) releases b if a

ScenarioOne {
  ACS = {
    },
  OBS = {
  }
}

ever b at 2 when ScenarioOne
always b at 2 when ScenarioOne

