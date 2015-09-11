initially (a && b)
(Janek, A) occurs at 3
(Janek, A) releases a
(Janek, B) occurs at 6
(Janek, B) releases b if a

ScenarioOne {
  ACS = {
    },
  OBS = {
  }
}

ever b at 9 when ScenarioOne
always b at 9 when ScenarioOne

//true, false