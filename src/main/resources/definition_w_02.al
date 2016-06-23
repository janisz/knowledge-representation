initially (loaded && alive)
(Janek, load) causes loaded
(Janek, spin) releases loaded
(Janek, shoot) causes -loaded
(Janek, shoot) causes -alive if loaded

ScenarioOne {
  ACS = {
      ((Janek, spin), 0),
      ((Janek, shoot), 1)
    },
  OBS = {
  }
}

always performed (Janek, spin) at 0 when ScenarioOne
always performed (Janek, shoot) at 1 when ScenarioOne
always -alive at 2 when ScenarioOne
always -loaded at 2 when ScenarioOne
ever performed (Janek, spin) at 0 when ScenarioOne
ever performed (Janek, shoot) at 1 when ScenarioOne
ever -alive at 2 when ScenarioOne
ever -loaded at 2 when ScenarioOne
