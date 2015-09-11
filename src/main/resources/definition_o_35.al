initially (a && b)
(Janek, A) occurs at 3
(Janek, A) releases a
(Janek, B) occurs at 6
(Janek, B) causes -b if a

ScenarioOne {
  ACS = {
    },
  OBS = {
    (b, 7)
  }
}

ScenarioTwo {
  ACS = {
    },
  OBS = {
    (b, 9)
  }
}

ever b at 10 when ScenarioOne
always b at 10 when ScenarioOne
ever b at 10 when ScenarioTwo
always b at 10 when ScenarioTwo
//null, null, true, true