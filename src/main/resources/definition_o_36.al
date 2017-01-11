initially (a && b)
(Janek, A) occurs at 3
(Janek, A) releases a
(Janek, B) occurs at 6
(Janek, B) invokes (Janek, C) after 3 if a

ScenarioOne {
  ACS = {
    },
  OBS = {
  }
}

ScenarioTwo {
  ACS = {
    ((Janek, D), 9)
    },
  OBS = {
  }
}

ever performed (Janek, C) at 9 when ScenarioOne
always performed (Janek, C) at 9 when ScenarioOne
ever performed (Janek, C) at 9 when ScenarioTwo
always performed (Janek, C) at 9 when ScenarioTwo

//true, false, null, null