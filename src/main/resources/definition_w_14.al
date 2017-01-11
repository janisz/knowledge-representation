(Janek, load) causes loaded
(Janek, shoot) causes -loaded
(Janek, shoot) causes -alive if (loaded && - hidden)
(Janek, load) invokes (turkey, escape)
(turkey, escape) releases hidden

ScenarioOne {
  ACS = {
      ((Janek, load), 0),
      ((Janek, shoot), 2)
    },
  OBS = {
  (((alive && -loaded) && -hidden), 0)
  }
}

always performed (Janek, load) at 0 when ScenarioOne
always performed (Janek, shoot) at 2 when ScenarioOne
always -alive at 3 when ScenarioOne
always -loaded at 3 when ScenarioOne
ever performed (Janek, load) at 0 when ScenarioOne
ever performed (Janek, shoot) at 2 when ScenarioOne
ever -alive at 3 when ScenarioOne
ever -loaded at 3 when ScenarioOne
