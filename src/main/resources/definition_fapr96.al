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

ScenarioTwo {
  ACS = {
        ((Train, Leave), 2)
    },
  OBS = {
  }
}

always performed (Janek, Buy) at 0 when ScenarioOne
always performed (Janek, GetIn) at 2 when ScenarioOne
always performed (Train, Leave) at 3 when ScenarioOne
always h at 4 when ScenarioOne
always i at 4 when ScenarioOne
always -p at 4 when ScenarioOne

ever performed (Janek, Buy) at 0 when ScenarioTwo
always performed (Train, Leave) at 2 when ScenarioTwo
typically performed (Train, Leave) at 2 when ScenarioTwo
ever ((-h && -i) && -p) at 3 when ScenarioTwo
ever ((h && -i) && -p) at 3 when ScenarioTwo
always (-i && -p) at 3 when ScenarioTwo
typically (-i && -p) at 3 when ScenarioTwo
