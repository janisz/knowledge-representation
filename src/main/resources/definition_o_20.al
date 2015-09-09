typically (Janek, A) invokes (Janek, B) after 3 if (loaded && alive)
(Janek, B) causes -loaded
(Janek, B) causes -alive if loaded

ScenarioOne {
  ACS = {
    ((Janek, A), 3)
    },
  OBS = {
	(loaded, 0),
	(alive, 0),
	(alive, 9)
  }
}

ever performed (Janek, B) at 6 when ScenarioOne