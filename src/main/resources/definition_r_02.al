(Jim, Shoot) causes -loaded
(Jim, Shoot) releases alive if loaded
(Bill, Shoot) releases alive if loaded

ScenarioOne {
  ACS = {
	((Jim, Shoot),0),
	((Bill,Shoot), 5)

    },
  OBS = {
	(loaded, 0),
	(alive, 0)
  }
}

ever alive at 6 when ScenarioOne