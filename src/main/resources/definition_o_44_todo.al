
(Janek, A) occurs at 3
(Janek, A) causes -b if a

ScenarioOne {
  ACS = {
    },
  OBS = {
	((a && b), 0)
  }
}

ScenarioTwo {
  ACS = {
    },
  OBS = {
	((-a && -b), 0)
  }
}

ever b at 10 when ScenarioOne
always b at 10 when ScenarioOne
ever b at 10 when ScenarioTwo
always b at 10 when ScenarioTwo
//null, null, true, true