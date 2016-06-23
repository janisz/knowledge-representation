typically (loaded && alive) triggers (Jim, Shoot)
(Jim, Shoot) causes -loaded
(Jim, Shoot) causes -alive if loaded

ScenarioOne {
  ACS = {

    },
  OBS = {
	(loaded, 0),
	(alive, 0),
	(alive, 4)
  }
}

ever performed (Jim, Shoot) at 0 when ScenarioOne