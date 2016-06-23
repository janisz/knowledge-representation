initially ((-h && p) && -i)
typically -h triggers (Janek, Buy)
typically (Janek, Buy) invokes (Janek, GetIn) after 2 if p
(Janek, Buy) causes h
(Janek, GetIn) causes i if h
(Train, Leave) causes -p

ScenarioOne {
  ACS = {
        ((Train, Leave), 3)
    },
  OBS = {
  }
}

typically performed (Janek, Buy) at 0 when ScenarioOne
typically performed (Janek, GetIn) at 2 when ScenarioOne
always performed (Train, Leave) at 3 when ScenarioOne
typically h at 4 when ScenarioOne
typically i at 4 when ScenarioOne
typically -p at 4 when ScenarioOne
ever ((h && i) && -p) at 4 when ScenarioOne
