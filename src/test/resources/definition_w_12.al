(Janek, A) causes f
(Janek, B) causes -f
(Janek, C) causes g
(Janek, A) invokes (Janek, C) after 2

ScenarioOne {
  ACS = {
      ((Janek, A), 0),
      ((Janek, B), 1)
    },
  OBS = {
  }
}

always performed (Janek, A) at 0 when ScenarioOne
always performed (Janek, B) at 1 when ScenarioOne

