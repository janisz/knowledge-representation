initially a
(Janek, A) occurs at 0
(Janek, A) releases a
(Janek, B) occurs at 1
(Janek, B) invokes (Janek, -C) after 1 if a

ScenarioOne {
  ACS = {
    ((Janek, C), 2)
    },
  OBS = {
  }
}

ScenarioTwo {
  ACS = {
    ((Janek, D), 2)
    },
  OBS = {
  }
}

ever performed (Janek, C) at 2 when ScenarioOne
always performed (Janek, C) at 2 when ScenarioOne
ever performed (Janek, D) at 2 when ScenarioTwo
always performed (Janek, D) at 2 when ScenarioTwo
