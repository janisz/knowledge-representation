initially ((-h && p) && -i)
-h triggers (Janek, Buy)
(Janek, Buy) invokes (Janek, GetIn) after 2 if p
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

always performed (Janek, Buy) at 0 when ScenarioOne
always performed (Janek, GetIn) at 2 when ScenarioOne
always performed (Train, Leave) at 3 when ScenarioOne
always h at 4 when ScenarioOne
always i at 4 when ScenarioOne
always -p at 4 when ScenarioOne

